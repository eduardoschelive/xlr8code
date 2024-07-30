package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PasswordAlreadyUsedException extends ApplicationException {
    public PasswordAlreadyUsedException() {
        super("The specified password has already been used");
    }


    @Override
    public String getErrorCode() {
        return "PASSWORD_ALREADY_USED";
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.password_already_used";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
