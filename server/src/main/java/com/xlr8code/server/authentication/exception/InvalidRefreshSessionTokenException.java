package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshSessionTokenException extends ApplicationException {

    public InvalidRefreshSessionTokenException() {
        super("The refresh session token provided is invalid");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_refresh_session_token";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_REFRESH_SESSION_TOKEN";
    }

}
