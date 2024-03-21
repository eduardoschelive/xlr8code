package com.xlr8code.server.series.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class SeriesNotFoundException extends ApplicationException {

    public SeriesNotFoundException(String uuid) {
        super("SERIES_NOT_FOUND", uuid);
    }

    @Override
    public String getMessageIdentifier() {
        return "series.error.not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
