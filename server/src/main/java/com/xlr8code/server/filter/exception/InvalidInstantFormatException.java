package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static com.xlr8code.server.filter.utils.FilterConstants.FILTER_VALUE_SEPARATOR;

public class InvalidInstantFormatException extends ApplicationException {

    public InvalidInstantFormatException(String value) {
        super("The specified value is not in the UTC format", value, Instant.now().toString());
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_instant_format";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_INSTANT_FORMAT";
    }

}
