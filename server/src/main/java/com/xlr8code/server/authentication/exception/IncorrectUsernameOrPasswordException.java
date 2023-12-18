package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class IncorrectUsernameOrPasswordException extends ApplicationException {

    public IncorrectUsernameOrPasswordException() {
        super("INCORRECT_USERNAME_OR_PASSWORD");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.incorrect_username_or_password";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}

