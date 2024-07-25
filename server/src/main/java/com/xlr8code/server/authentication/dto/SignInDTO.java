package com.xlr8code.server.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name= "SignIn")
public record SignInDTO(
        @Schema(description = "The user's login (username or email)")
        @NotBlank
        String login,
        @Schema(description = "The user's current password")
        @NotBlank
        String password,
        @Schema(description = "Whether to remember the user's session")
        @NotNull
        boolean rememberMe
) {
}
