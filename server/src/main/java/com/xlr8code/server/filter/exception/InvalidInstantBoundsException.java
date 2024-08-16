package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidInstantBoundsException extends ApplicationException {

    public InvalidInstantBoundsException(String lowerBound, String upperBound) {
        super("The lower bound must be before the upper bound", lowerBound, upperBound);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_instant_bounds";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_INSTANT_BOUNDS";
    }

}
