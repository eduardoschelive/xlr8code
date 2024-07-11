package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NoMatchingEntitiesFoundException extends ApplicationException {

    public NoMatchingEntitiesFoundException() {
        super("NO_MATCHING_ENTITIES_FOUND");
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.no_matching_entities_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
