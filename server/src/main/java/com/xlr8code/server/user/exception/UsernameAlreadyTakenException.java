package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyTakenException extends ApplicationException {

    public UsernameAlreadyTakenException() {
        super("USERNAME_ALREADY_TAKEN");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.username_already_taken";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

}
