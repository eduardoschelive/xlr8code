package com.xlr8code.server.common.dto;

import java.time.Instant;

public record ApplicationExceptionResponseDTO(
        Integer status,
        String error,
        String message,
        Instant timestamp,
        String path
) {
}
