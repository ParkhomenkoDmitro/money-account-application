package com.parkhomenko;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */
@RestController
@RequestMapping("/transactions")
@Api(
        value = "Money transaction server",
        produces = "application/json",
        consumes = "application/json",
        tags = "Transaction resourse", description = "Transaction resourse API")
public class TransactionResourse {

    @Autowired
    private TransactionService transactionService;

    @ApiOperation(value = "Create one transaction", notes = "Here you can create one debit or credit transaction for oraganization",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created one transaction")
        ,
        @ApiResponse(code = 400, message = "Transaction is not valid due to possible reasons: "
                + "1) if 'errorCode' equal 'INVALID_JSON_ERROR' it means that you have invalid json structure or invalid json data types"
                + "2) if 'errorCode' equal 'NEGATIVE_AMOUNT_SYSTEM_ERROR' it means that any transaction, which leads to negative amount in the system, should be refused"
                + "3) if 'errorCode' equal 'ZERO_AMOUNT_ERROR' it means that transaction amount is zero"
                + "4) if 'errorCode' equal 'IMVALID_ORGANIZATION_ERROR' it means that organization id in transaction is invalid"
        )
    })
    @PostMapping
    public void createTransaction(@RequestBody TransactionDto dto) {
        transactionService.createTransaction(dto);
    }

    @ApiOperation(value = "Get default organization", notes = "Get default organization",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get default organization")
    })
    @GetMapping("/organization")
    public OrganizationDto getOrganization() {
        return transactionService.getOrganization();
    }

    @ApiOperation(value = "Get transaction list", notes = "Here you can get transaction list with pagination",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get transaction list")
    })
    @GetMapping
    public GridResponse<TransactionDto> getTransactions(Pageable pageRequest) {
        return transactionService.getTransactions(pageRequest);
    }
}
