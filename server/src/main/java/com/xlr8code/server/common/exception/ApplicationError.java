package com.xlr8code.server.common.exception;

import lombok.Getter;

@Getter
public class ApplicationError extends RuntimeException {

    private final ErrorCode errorCode;

    public ApplicationError(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationError(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}