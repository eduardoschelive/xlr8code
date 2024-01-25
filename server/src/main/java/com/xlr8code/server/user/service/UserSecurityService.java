package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

    public boolean canModifyResource(UserDetails principal, String userId) {
        var user = (User) principal;

        if (user.isAdmin()) return true;

        var userIdString = user.getId().toString();

        return userIdString.equals(userId);
    }

}
