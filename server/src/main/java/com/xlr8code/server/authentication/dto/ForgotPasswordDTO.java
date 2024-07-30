package com.xlr8code.server.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ForgotPassword")
public record ForgotPasswordDTO(
        @Schema(description = "The user's login (username or email)")
        @NotBlank
        String login
) {
}
