package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

public class SlugAlreadyExistsException extends ApplicationException {

    public SlugAlreadyExistsException(String slug) {
        super("The specified slug is already used by another resource on the same path", slug);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.slug_already_exists";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "SLUG_ALREADY_EXISTS";
    }

}
