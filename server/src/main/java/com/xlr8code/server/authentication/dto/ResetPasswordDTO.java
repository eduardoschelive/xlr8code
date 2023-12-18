package com.xlr8code.server.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO(
        @NotBlank
        String code,
        @NotBlank
        String newPassword,
        @NotBlank
        String newPasswordConfirmation
) {
}
