package com.xlr8code.server.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ResetPassword")
public record ResetPasswordDTO(
        @Schema(description = "The code sent to the user.")
        @NotBlank
        String code,
        @Schema(description = "The new user's password.")
        @NotBlank
        String newPassword,
        @Schema(description = "The new user's password confirmation. Must match the `newPassword` field.")
        @NotBlank
        String newPasswordConfirmation
) {
}
