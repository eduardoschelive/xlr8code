package com.xlr8code.server.common.exception;

import java.util.Date;

public record ApplicationExceptionResponse(
        String message,
        Date timestamp
) {
}
