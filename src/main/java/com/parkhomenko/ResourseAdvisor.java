package com.parkhomenko;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author dmytro
 */
@ControllerAdvice
public class ResourseAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourseAdvisor.class);

    public static enum CommonErrorCode {
        INVALID_JSON_ERROR
    }

    @ExceptionHandler({TransactionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM resolveException1(TransactionException ex) {
        LOGGER.error(ex.getClass().getSimpleName(), ex);
        return new ErrorVM(ex.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM resolveException1(MissingServletRequestParameterException ex) {
        LOGGER.error(ex.getClass().getSimpleName(), ex);
        return new ErrorVM(CommonErrorCode.INVALID_JSON_ERROR.name());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM resolveException(HttpMessageNotReadableException ex) {
        LOGGER.error(ex.getClass().getSimpleName(), ex);
        return new ErrorVM(CommonErrorCode.INVALID_JSON_ERROR.name());
    }

    @ToString
    public static class ErrorVM {

        public final String errorCode;

        public ErrorVM(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
