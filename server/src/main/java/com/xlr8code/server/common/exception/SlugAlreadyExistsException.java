package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

public class SlugAlreadyExistsException extends ApplicationException {

    public SlugAlreadyExistsException(String slug) {
        super("SLUG_ALREADY_EXISTS", slug);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.slug_already_exists";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
