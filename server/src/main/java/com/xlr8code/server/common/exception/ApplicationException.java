package com.xlr8code.server.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionType errorCode;

    public ApplicationException(ExceptionType errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

}
