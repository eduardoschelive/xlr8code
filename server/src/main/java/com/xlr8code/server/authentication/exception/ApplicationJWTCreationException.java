package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ApplicationJWTCreationException extends ApplicationException {

    public ApplicationJWTCreationException() {
        super("JWT_CREATION_ERROR");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.jwt_creation_error";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}

