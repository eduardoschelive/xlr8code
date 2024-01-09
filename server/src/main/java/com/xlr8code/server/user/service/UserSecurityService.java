package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

    public boolean canModifyResource(Authentication authentication, String userId) {
        var user = (User) authentication.getPrincipal();
        var uuidOptional = UUIDUtils.convertFromString(userId);

        if (uuidOptional.isEmpty()) {
            return false;
        }

        var uuid = uuidOptional.get();

        return user.getId().equals(uuid) && user.isAdmin();
    }

}
