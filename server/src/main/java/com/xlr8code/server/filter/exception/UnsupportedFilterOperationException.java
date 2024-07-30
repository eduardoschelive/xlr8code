package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.filter.enums.FilterOperation;
import org.springframework.http.HttpStatus;

public class UnsupportedFilterOperationException extends ApplicationException {

    public UnsupportedFilterOperationException(String operation) {
        super("The specified filter operation is not supported", operation, getSupportedOperations());
    }

    private static String getSupportedOperations() {
        return String.join(", ", FilterOperation.OPERATIONS_MAP.keySet());
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.unsupported_filter_operation";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "UNSUPPORTED_FILTER_OPERATION";
    }

}
