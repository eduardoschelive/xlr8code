package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotNull
        Theme themePreference,
        @NotNull
        Language languagePreference,
        @Nullable
        String profilePictureUrl
) {
}