package com.xlr8code.server.authentication.dto.activation;

import jakarta.validation.constraints.NotBlank;

public record ResendCodeRequestDTO(
        @NotBlank
        String login
) {
}
