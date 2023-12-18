package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ExpiredPasswordResetCodeException extends ApplicationException {

    public ExpiredPasswordResetCodeException() {
        super("EXPIRED_PASSWORD_RESET_CODE");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.expired_password_reset_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}

