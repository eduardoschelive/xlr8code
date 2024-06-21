package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidBooleanFilterValueException extends ApplicationException {

    public InvalidBooleanFilterValueException(String value) {
        super("INVALID_BOOLEAN_FILTER_VALUE", value);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_boolean_filter_value";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
