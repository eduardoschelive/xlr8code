package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidNewPasswordException extends ApplicationException {

    public InvalidNewPasswordException() {
        super("INVALID_NEW_PASSWORD");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.invalid_new_password";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
