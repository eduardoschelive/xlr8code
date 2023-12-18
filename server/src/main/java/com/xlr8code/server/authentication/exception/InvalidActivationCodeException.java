package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidActivationCodeException extends ApplicationException {

    public InvalidActivationCodeException() {
        super("INVALID_ACTIVATION_CODE");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_activation_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
