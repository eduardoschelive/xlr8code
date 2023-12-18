package com.xlr8code.server.common.exception;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class ApplicationError extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
