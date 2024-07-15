package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class LanguageNotFoundException extends ApplicationException {

    public LanguageNotFoundException(String languageCode) {
        super("The specified language code was not found or is not supported", languageCode);
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.language_not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return "LANGUAGE_NOT_FOUND";
    }

}