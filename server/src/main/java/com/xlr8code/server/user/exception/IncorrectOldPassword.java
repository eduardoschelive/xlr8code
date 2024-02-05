package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class IncorrectOldPassword extends ApplicationException {

    public IncorrectOldPassword() {
        super("INCORRECT_OLD_PASSWORD");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.incorrect_old_password";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

