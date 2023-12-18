package com.xlr8code.server.common.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionType exceptionType;
    @Nullable
    private final Object[] placeholders;

    public ApplicationException(ExceptionType exceptionType) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
        this.placeholders = null;
    }

    public ApplicationException(ExceptionType exceptionType, @Nullable Object... placeholders) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
        this.placeholders = placeholders;
    }

}
