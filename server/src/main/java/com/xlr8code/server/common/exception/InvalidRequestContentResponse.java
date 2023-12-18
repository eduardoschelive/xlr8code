package com.xlr8code.server.common.exception;

import java.util.Date;
import java.util.Map;

public record InvalidRequestContentResponse(
        String message,
        Date timestamp,
        Map<String, String> errors
) {
}
