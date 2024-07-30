package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ExpiredActivationCodeException extends ApplicationException {

    public ExpiredActivationCodeException() {
        super("The activation code has expired and cannot be used for account activation");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.expired_activation_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "EXPIRED_ACTIVATION_CODE";
    }

}

