package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidSortDirectionException extends ApplicationException {

    public InvalidSortDirectionException(String direction) {
        super("The specified sort direction is not valid", direction);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_sort_direction";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_SORT_DIRECTION";
    }

}
