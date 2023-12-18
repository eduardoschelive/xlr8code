package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ExpiredActivationCodeException extends ApplicationException {

    public ExpiredActivationCodeException() {
        super("EXPIRED_ACTIVATION_CODE");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.expired_activation_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}

