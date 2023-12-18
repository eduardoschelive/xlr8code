package com.xlr8code.server.authentication.dto.password;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(
        @NotBlank
        String login
) {
}
