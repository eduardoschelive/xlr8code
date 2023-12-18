package com.xlr8code.server.user.utils;

import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

@RequiredArgsConstructor
public class RoleEndpoints {



    public static Map<UserRole, String[]> getRoleEndpoints() {

        var endpoints = new EnumMap<UserRole, String[]>(UserRole.class);

        endpoints.put(UserRole.MEMBER, new String[]{"/api/member"});
        endpoints.put(UserRole.ADMIN, new String[]{"/api/admin"});


        return endpoints;

    }

}
