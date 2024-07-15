package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserSessionCreationException extends ApplicationException {

    public UserSessionCreationException() {
        super("An error occurred in the server while creating the user session");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.session_creation";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorCode() {
        return "USER_SESSION_CREATION_ERROR";
    }

}
