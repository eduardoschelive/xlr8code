package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.Role;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateUserDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotNull
        Set<Role> roles,
        @NotNull
        Theme themePreference,
        @NotNull
        Language languagePreference,
        @Nullable
        String profilePictureUrl,
        Boolean active
) {
    public User toUserWithMetadata() {
        var user = User.builder()
                .username(this.username())
                .email(this.email())
                .password(this.password())
                .roles(this.roles())
                .active(this.active())
                .build();

        var userMetadata = UserMetadata.builder()
                .languagePreference(this.languagePreference())
                .themePreference(this.themePreference())
                .profilePictureUrl(this.profilePictureUrl())
                .user(user)
                .build();

        user.setMetadata(userMetadata);

        return user;
    }

}
