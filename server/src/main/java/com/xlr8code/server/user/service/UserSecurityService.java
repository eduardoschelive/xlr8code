package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

    /**
     * @param principal the principal of the current request
     * @param userId    the ID of the user whose resource is to be modified
     * @return whether the principal can modify the resource of the user with the given ID
     */
    public boolean canModifyResource(UserDetails principal, String userId) {
        var user = (User) principal;

        if (user.isAdmin()) return true;

        var userIdString = user.getId().toString();

        return userIdString.equals(userId);
    }

}
