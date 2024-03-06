package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateSlugInLanguagesException extends ApplicationException {

    public DuplicateSlugInLanguagesException(String slug) {
        super("DUPLICATE_SLUG_IN_LANGUAGES", slug);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.duplicate_slug_in_languages";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
