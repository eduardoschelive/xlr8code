package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoSuchFilterableFieldException extends ApplicationException {

    public NoSuchFilterableFieldException(String field) {
        super("The specified field is not filterable", field);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.no_such_filterable_field";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "NO_SUCH_FILTERABLE_FIELD";
    }

}
