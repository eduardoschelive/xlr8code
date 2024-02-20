package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.dto.UserPreferencesDTO;
import com.xlr8code.server.user.utils.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record SignUpDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotNull
        UserPreferencesDTO preferences,
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