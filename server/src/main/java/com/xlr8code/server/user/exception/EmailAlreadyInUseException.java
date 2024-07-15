package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends ApplicationException {

    public EmailAlreadyInUseException() {
        super("The email provided is already in use by another user account");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.email_already_in_use";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getErrorCode() {
        return "EMAIL_ALREADY_IN_USE";
    }

}
