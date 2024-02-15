package com.xlr8code.server.common.dto;

import java.time.Instant;
import java.util.Map;

public record ApplicationFieldExceptionDTO(
        Integer status,
        String error,
        String message,
        Instant timestamp,
        String path,
        Map<String, String> errors
) {
}
