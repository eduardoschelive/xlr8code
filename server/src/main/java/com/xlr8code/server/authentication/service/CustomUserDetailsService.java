package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        var user = this.userService.findByLogin(login);
        if (user.isEmpty()) {
            throw new ApplicationException(AuthenticationExceptionType.INCORRECT_USERNAME_OR_PASSWORD, login);
        }

        return user.get();
    }


}
