package com.xlr8code.server.common.dto;

import java.util.Date;
import java.util.Map;

public record ApplicationFieldExceptionDTO(
        Integer status,
        String error,
        String message,
        Date timestamp,
        Map<String, String> errors
) {
}
