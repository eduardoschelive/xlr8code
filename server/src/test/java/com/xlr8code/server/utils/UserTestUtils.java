package com.xlr8code.server.utils;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UpdateUserDTO;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.entity.Role;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.entity.UserPassword;
import com.xlr8code.server.user.utils.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

public class UserTestUtils {

    public static CreateUserDTO buildCreateUserDTO(String username, String email, String password) {
        return buildCreateUserDTO(username, email, password, false);
    }

    public static CreateUserDTO buildCreateUserDTO(String username, String email, String password, boolean active) {
        return new CreateUserDTO(
                username,
                email,
                password,
                Set.of(UserRole.DEFAULT.toRole()),
                Theme.SYSTEM,
                Language.AMERICAN_ENGLISH,
                null,
                active
        );
    }

    public static User buildUser(String username, String email, String password, PasswordEncoder passwordEncoder) {
        UserMetadata metadata = UserMetadata.builder()
                .themePreference(Theme.SYSTEM)
                .languagePreference(Language.AMERICAN_ENGLISH)
                .build();

        return User.builder()
                .username(username)
                .email(email)
                .userPassword(new UserPassword(password, passwordEncoder))
                .metadata(metadata)
                .build();
    }

    public static User buildUserDetails(UUID id, Set<Role> roles, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(id)
                .roles(roles)
                .userPassword(new UserPassword("password", passwordEncoder))
                .active(true)
                .build();
    }

    public static UpdateUserDTO buildUpdateUserDTO(String username, String email) {
        return new UpdateUserDTO(username, email);
    }

    public static UpdateUserMetadataDTO buildUpdateUserMetadataDTO(Theme theme, Language language, String profilePictureUrl) {
        return new UpdateUserMetadataDTO(theme, language, profilePictureUrl);
    }
}
