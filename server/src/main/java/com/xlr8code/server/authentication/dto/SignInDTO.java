package com.xlr8code.server.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignInDTO(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        boolean rememberMe
) {
}
