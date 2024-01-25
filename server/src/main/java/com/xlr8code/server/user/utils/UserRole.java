package com.xlr8code.server.user.utils;

import com.xlr8code.server.user.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    ADMIN(1L, "ROLE_ADMIN"),
    MEMBER(2L, "ROLE_MEMBER");

    public static final UserRole DEFAULT = MEMBER;
    private final Long id;
    private final String value;

    public Role toRole() {
        return Role.builder().id(this.id).userRole(this).build();
    }

    public static String getHierarchy() {
        return ADMIN.value + " > " + MEMBER.value;
    }

}
