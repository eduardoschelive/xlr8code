package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ExpiredPasswordResetCodeException extends ApplicationException {

    public ExpiredPasswordResetCodeException() {
        super("The password reset code has expired and cannot be used for password reset");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.expired_password_reset_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "EXPIRED_PASSWORD_RESET_CODE";
    }

}

