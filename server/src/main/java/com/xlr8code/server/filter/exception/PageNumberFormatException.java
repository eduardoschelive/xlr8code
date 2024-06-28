package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PageNumberFormatException extends ApplicationException {

    public PageNumberFormatException(String parameterName) {
        super("FILTER_ERROR_PAGE_NUMBER_FORMAT", parameterName);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_page_number_format";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
