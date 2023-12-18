package com.xlr8code.server.common.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionType errorCode;
    @Nullable
    private final Object[] placeholders;

    public ApplicationException(ExceptionType errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.placeholders = null;
    }

    public ApplicationException(ExceptionType errorCode, @Nullable Object... placeholders) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.placeholders = placeholders;
    }

}
