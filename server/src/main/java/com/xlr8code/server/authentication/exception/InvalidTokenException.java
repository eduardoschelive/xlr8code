package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApplicationException {

    public InvalidTokenException() {
        super("INVALID_TOKEN");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_token";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
