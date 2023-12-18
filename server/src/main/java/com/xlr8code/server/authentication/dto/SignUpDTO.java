package com.xlr8code.server.authentication.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String themePreference,
        @NotBlank
        String languagePreference,
        @Nullable
        String profilePictureUrl
) {
}