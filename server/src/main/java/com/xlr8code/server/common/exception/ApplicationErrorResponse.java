package com.xlr8code.server.common.exception;

import java.util.Date;

public record ApplicationErrorResponse(
        int internalCode,
        String message,
        Date timestamp
) {
}
