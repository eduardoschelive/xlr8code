package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.user_already_exists";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }


}
