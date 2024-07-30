package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateSlugInLanguagesException extends ApplicationException {

    public DuplicateSlugInLanguagesException(String slug) {
        super("The slugs of multilanguage content should be unique to avoid conflicts in the url", slug);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.duplicate_slug_in_languages";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "DUPLICATE_SLUG_IN_LANGUAGES";
    }

}
