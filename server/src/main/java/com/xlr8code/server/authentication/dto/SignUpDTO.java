package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.dto.UserPreferencesDTO;
import com.xlr8code.server.user.utils.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(name = "SignUp")
public record SignUpDTO(
        @Schema(description = "The username of the user")
        @NotBlank
        String username,
        @Schema(description = "The email of the user")
        @NotBlank
        @Email
        String email,
        @Schema(description = "The password of the user")
        @NotBlank
        String password,
        @Schema(description = "The preferences of the user")
        @NotNull
        UserPreferencesDTO preferences,
        @Schema(description = "The metadata of the user")
        @NotNull
        UserMetadataDTO metadata
) {
    public CreateUserDTO toCreateUserDTO() {
        return new CreateUserDTO(
                this.username(),
                this.email(),
                this.password(),
                Set.of(UserRole.MEMBER.toRole()),
                this.metadata(),
                this.preferences(),
                false
        );
    }

}