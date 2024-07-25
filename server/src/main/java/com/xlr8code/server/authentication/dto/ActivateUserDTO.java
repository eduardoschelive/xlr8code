package com.xlr8code.server.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ActivateUser")
public record ActivateUserDTO(
        @Schema(description = "The activation code sent to the user.")
        @NotBlank
        String code
) {
}
