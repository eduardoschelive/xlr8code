package com.xlr8code.server.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "ApplicationExceptionResponse", description = "The response of an application exception that is thrown by the server")
public record ApplicationExceptionResponseDTO(
        @Schema(description = "The HTTP status code of the response")
        Integer status,
        @Schema(description = "The error of the response. Can be used to check the examples or contact the support")
        String error,
        @Schema(description = "The message of the response. Can be used to display the error message to the user")
        String message,
        @Schema(description = "The timestamp of the response. Can be used to check the time of the error")
        Instant timestamp,
        @Schema(description = "The path of the response. Can be used to check the path of the error")
        String path
) {
}
