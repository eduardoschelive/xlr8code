package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class SessionExpiredException extends ApplicationException {

    public SessionExpiredException() {
        super("The session has expired and the user needs to login again");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.session_expired";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return "SESSION_EXPIRED";
    }

}
