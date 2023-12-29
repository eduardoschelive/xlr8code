package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidToken extends ApplicationException {

    public InvalidToken() {
        super("INVALID_TOKEN");
    }

    @Override
    public String getMessageIdentifier() {
        return "error.authentication.invalid_token";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
