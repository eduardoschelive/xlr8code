package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoSuchSortableFieldException extends ApplicationException {

    public NoSuchSortableFieldException(String fieldName) {
        super("NO_SUCH_SORTABLE_FIELD", fieldName);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.no_such_sortable_field";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
