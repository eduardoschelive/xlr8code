package com.xlr8code.server.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequestDTO(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
