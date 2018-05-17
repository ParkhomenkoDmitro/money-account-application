package com.parkhomenko;

import com.parkhomenko.TransactionException.ErrorCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmytro
 */

@Service
public class TransactionService {
    
    private static final int MAX_AMOUNT_LEGTH = 19;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    /**
     * Optimistic locking is used for parallel transactions
     * dirty save will save organization entity
     * @param dto - transaction DTO
     */
    @Transactional
    public void createTransaction(TransactionDto dto) {
        Organization organization = validate(dto);
        
        if(dto.isCredit) {
            organization.amount = organization.amount.subtract(dto.amount);
        } else {
            organization.amount = organization.amount.add(dto.amount);
        }
        
        transactionRepository.save(new Transaction(LocalDateTime.now(), 
                dto.isCredit, 
                organization, 
                dto.amount
        ));
    }

    @Transactional(readOnly = true)
    public OrganizationDto getOrganization() {
        List<Organization> organizations = organizationRepository.findAll();
        Organization organization = organizations.get(0);
        OrganizationDto result = new OrganizationDto(organization.id, organization.name, organization.amount);
        return result;
    }
    
    @Transactional(readOnly = true)
    public GridResponse<TransactionDto> getTransactions(Pageable pageRequest) {
        Page<Transaction> page = transactionRepository.findAll(pageRequest);
        
        return new GridResponse<>(page.getContent(), item -> new TransactionDto(item.id,
                        item.date.format(dateTimeFormatter),
                        item.isCredit,
                        item.amount, 
                        item.organization.id), page.getTotalElements());    
    }
    
    private Organization validate(TransactionDto dto) {
        final long orgId = dto.organizationId;
        final Organization organization;
        
        try {
            organization = organizationRepository.findById(orgId).get();
        } catch (EntityNotFoundException e) {
            throw new TransactionException(ErrorCode.INVALID_ORGANIZATION_ERROR);
        }
        
        if(dto.amount == null || dto.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new TransactionException(ErrorCode.ZERO_AMOUNT_ERROR);
        }

	if(dto.amount.toPlainString().length() > MAX_AMOUNT_LEGTH) {
            throw new TransactionException(ErrorCode.VERY_BIG_DEBET_ERROR);
        }
        
        if(dto.isCredit) {
            if(organization.amount.compareTo(dto.amount) < 0) {
                throw new TransactionException(ErrorCode.NEGATIVE_AMOUNT_SYSTEM_ERROR);
            }
        }
        
        return organization;
    }
}