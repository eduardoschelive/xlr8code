package com.xlr8code.server.user.utils;

import com.xlr8code.server.user.entity.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleUtils {

    private static final List<Role> DEFAULTS = Arrays.asList(
            Role.builder().id(1L).name("ROLE_ADMIN").build(),
            Role.builder().id(2L).name("ROLE_USER").build()
    );

    public static List<Role> getDefaults() {
        return DEFAULTS;
    }

}
