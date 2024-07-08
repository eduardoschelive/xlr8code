package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoStrategyDefinedException extends ApplicationException {

    public NoStrategyDefinedException(String fieldName) {
        super("NO_STRATEGY_DEFINED", fieldName);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.no_strategy_defined";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
