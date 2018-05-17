package com.parkhomenko;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author dmytro
 */

@ToString
@EqualsAndHashCode
public class TransactionDto {
    
    public final Long id;
    public final String creation;
    public final Boolean isCredit;
    public final BigDecimal amount;
    public final Long organizationId;
    
    public TransactionDto() {
        id = 0L;
        creation = "";
        isCredit = false;
        amount = BigDecimal.ZERO;
        organizationId = 0L; 
    }

    public TransactionDto(Long id, String creation, Boolean isCredit, BigDecimal amount, Long organizationId) {
        this.id = id;
        this.creation = creation;
        this.isCredit = isCredit;
        this.amount = amount;
        this.organizationId = organizationId;
    }
}
