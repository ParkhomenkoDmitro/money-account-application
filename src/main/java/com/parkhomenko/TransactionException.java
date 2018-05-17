package com.parkhomenko;

/**
 *
 * @author dmytro
 */
public class TransactionException extends RuntimeException {
    
    public static enum ErrorCode {
        NEGATIVE_AMOUNT_SYSTEM_ERROR,
        ZERO_AMOUNT_ERROR,
        INVALID_ORGANIZATION_ERROR,
	VERY_BIG_DEBET_ERROR
    }

    public TransactionException(ErrorCode errorCode) {
        super(errorCode.name());
    }
}
