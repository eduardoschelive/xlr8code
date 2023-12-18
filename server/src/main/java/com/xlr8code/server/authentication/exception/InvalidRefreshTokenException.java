package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApplicationException {

    public InvalidRefreshTokenException() {
        super("INVALID_REFRESH_TOKEN");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_refresh_token";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
