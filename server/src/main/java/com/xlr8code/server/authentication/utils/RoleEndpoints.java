package com.xlr8code.server.authentication.utils;

import com.xlr8code.server.user.utils.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleEndpoints {

    public static Map<UserRole, String[]> getRoleEndpoints() {
        var endpoints = new EnumMap<UserRole, String[]>(UserRole.class);

        endpoints.put(UserRole.MEMBER, new String[]{Endpoint.FAKE});

        return endpoints;
    }

}