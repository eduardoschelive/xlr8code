package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class LanguageNotFoundException extends ApplicationException {

    public LanguageNotFoundException(String languageCode) {
        super("LANGUAGE_NOT_FOUND", languageCode);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.language_not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}