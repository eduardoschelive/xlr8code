package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserSecurityService {

    public boolean canModifyResource(Authentication authentication, String userId) {
        var user = (User) authentication.getPrincipal();
        var userUUID = UUID.fromString(userId);

        return user.getId().equals(userUUID) || user.isAdmin();
    }

}
