package com.xlr8code.server.common.exception;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

/**
 * Base class for all application exceptions.
 * @implNote All classes that extends this, should have a static string field named "DOCUMENTATION_EXAMPLE" that contains a string in json format that represents the exception
 */
public abstract class ApplicationException extends RuntimeException {

    private final Object[] placeholders;

    /**
     * @param message      the message of the exception
     * @param placeholders the placeholders to be replaced in the message
     */
    protected ApplicationException(String message, Object... placeholders) {
        super(message);
        this.placeholders = placeholders;
    }

    /**
     * @return the message identifier of the exception (e.g. "authentication.error.session_expired")
     */
    public abstract String getMessageIdentifier();

    /**
     * @return the {@link HttpStatus} corresponding to the exception
     */
    public abstract HttpStatus getHttpStatus();

    /**
     * @return the placeholders to be replaced in the message, if any, otherwise null
     */
    @Nullable
    public Object[] getPlaceholders() {
        return placeholders;
    }

}
