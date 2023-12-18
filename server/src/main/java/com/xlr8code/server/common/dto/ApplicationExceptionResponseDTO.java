package com.xlr8code.server.common.dto;

import java.util.Date;

public record ApplicationExceptionResponseDTO(
        Integer status,
        String error,
        String message,
        Date timestamp
) {
}
