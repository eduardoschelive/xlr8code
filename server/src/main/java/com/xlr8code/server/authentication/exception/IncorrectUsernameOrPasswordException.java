package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class IncorrectUsernameOrPasswordException extends ApplicationException {

    public IncorrectUsernameOrPasswordException() {
        super("The username or password provided is incorrect");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.incorrect_username_or_password";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return "INCORRECT_USERNAME_OR_PASSWORD";
    }

}

