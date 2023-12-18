package com.xlr8code.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final Object[] placeholders;

    protected ApplicationException(String message, Object... placeholders) {
        super(message);
        this.placeholders = placeholders;
    }

    public abstract String getMessageIdentifier();

    public abstract HttpStatus getHttpStatus();

}
