package com.xlr8code.server.article.service;

import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleSecurityService {

    public boolean canModifyResource(UserDetails principal) {
        var user = (User) principal;

        return user.isAdmin();
    }

}
