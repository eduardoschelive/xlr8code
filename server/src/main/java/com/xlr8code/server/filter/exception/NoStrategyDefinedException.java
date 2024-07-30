package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoStrategyDefinedException extends ApplicationException {

    public NoStrategyDefinedException(String fieldName) {
        super("No strategy has been defined for the specified field", fieldName);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.no_strategy_defined";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorCode() {
        return "NO_STRATEGY_DEFINED";
    }

}
