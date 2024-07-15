package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyTakenException extends ApplicationException {

    public UsernameAlreadyTakenException() {
        super("The specified username is already taken by another user account");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.username_already_taken";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getErrorCode() {
        return "USERNAME_ALREADY_TAKEN";
    }

}
