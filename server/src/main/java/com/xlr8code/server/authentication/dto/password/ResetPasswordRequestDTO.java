package com.xlr8code.server.authentication.dto.password;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(
        @NotBlank
        String newPassword,

        @NotBlank
        String code
) {
}
