package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super("The specified user was not found");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.user_not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return "USER_NOT_FOUND";
    }

}

