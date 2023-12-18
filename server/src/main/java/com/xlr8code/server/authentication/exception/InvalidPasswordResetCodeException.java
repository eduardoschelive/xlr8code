package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordResetCodeException extends ApplicationException {

    public InvalidPasswordResetCodeException() {
        super("INVALID_PASSWORD_RESET_CODE");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_password_reset_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}

