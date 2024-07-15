package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PasswordMatchException extends ApplicationException {

    public PasswordMatchException() {
        super("The passwords provided do not match each other");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.passwords_do_not_match";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "PASSWORDS_DO_NOT_MATCH";
    }

}
