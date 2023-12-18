package com.xlr8code.server.common.exception;

import java.util.Date;

public record ApplicationErrorResponse(
        String message,
        Date timestamp
) {
}
