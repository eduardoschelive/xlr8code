package com.xlr8code.server.common.exception;

import lombok.Getter;

@Getter
public class ApplicationError extends RuntimeException {

    private final ErrorCode errorCode;

    public ApplicationError(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

}
