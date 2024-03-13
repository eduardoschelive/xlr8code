package com.xlr8code.server.common.exception;

import com.xlr8code.server.common.enums.Language;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class LanguageAlreadyExistsForResourceException extends ApplicationException {

    public LanguageAlreadyExistsForResourceException(Language language, UUID resourceId) {
        super("LANGUAGE_ALREADY_EXISTS_FOR_RESOURCE", language, resourceId);
    }

    @Override
    public String getMessageIdentifier() {
        return "common.error.language_already_exists_for_resource";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
