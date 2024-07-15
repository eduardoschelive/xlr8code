package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UnsupportedFilterOperationOnFieldException extends ApplicationException {

    public UnsupportedFilterOperationOnFieldException(String field, String operation) {
        super("The operation you're trying to use is not supported by the specified field", field, operation);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.unsupported_filter_operation_on_field";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "UNSUPPORTED_FILTER_OPERATION_ON_FIELD";
    }

}
