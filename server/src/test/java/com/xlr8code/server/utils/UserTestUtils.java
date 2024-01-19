package com.xlr8code.server.utils;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.utils.UserRole;

import java.util.Set;

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

    public static User buildUser(String username, String email, String password) {
        var metadata = UserMetadata.builder()
                .themePreference(Theme.SYSTEM)
                .languagePreference(Language.AMERICAN_ENGLISH)
                .build();

        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .metadata(metadata)
                .build();
    }

}
