package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidActivationCodeException extends ApplicationException {

    public InvalidActivationCodeException() {
        super("The activation code provided is invalid");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.invalid_activation_code";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_ACTIVATION_CODE";
    }

}
