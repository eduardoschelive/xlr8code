package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BadFilterFormatException extends ApplicationException {

    public BadFilterFormatException() {
        super("BAD_FILTER_FORMAT");
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.bad_filter_format";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
