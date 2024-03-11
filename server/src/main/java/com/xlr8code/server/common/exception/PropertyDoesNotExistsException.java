package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

public class PropertyDoesNotExistsException  extends ApplicationException{

    public PropertyDoesNotExistsException(String propertyName) {
        super("PROPERTY_DOES_NOT_EXISTS", propertyName);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.property_does_not_exists";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
