package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PageNumberFormatException extends ApplicationException {

    public PageNumberFormatException(String parameterName) {
        super("The specified page number is not a valid number", parameterName);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.invalid_page_number_format";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_PAGE_NUMBER_FORMAT";
    }

}
