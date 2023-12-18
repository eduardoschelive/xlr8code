package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends ApplicationException {

    public EmailAlreadyInUseException() {
        super("EMAIL_ALREADY_IN_USE");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.email_already_in_use";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
