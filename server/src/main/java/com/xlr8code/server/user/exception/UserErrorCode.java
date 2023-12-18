package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    ;

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
