package com.xlr8code.server.category.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApplicationException {

    public CategoryNotFoundException(String uuid) {
        super("CATEGORY_NOT_FOUND", uuid);
    }

    @Override
    public String getMessageIdentifier() {
        return "category.error.not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
