package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.Role;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        @NotNull UserMetadataDTO metadata,
        @NotNull
        UserPreferencesDTO preferences,
        Boolean active
) {
    public User toUser(PasswordEncoder passwordEncoder) {
        var user = User.builder()
                .username(this.username())
                .email(this.email())
                .userPassword(new UserPassword(this.password(), passwordEncoder))
                .roles(this.roles())
                .active(this.active())
                .build();

        var userMetadata = this.metadata().toUserMetadata(user);

        var userPreferences = this.preferences().toUserPreferences(user);

        user.setPreferences(userPreferences);
        user.setMetadata(userMetadata);

        return user;
    }

}
