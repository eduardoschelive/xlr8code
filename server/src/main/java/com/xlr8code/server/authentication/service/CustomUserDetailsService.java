package com.xlr8code.server.authentication.service;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.exception.UserExceptionType;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = this.userService.findByLogin(login);
        if (user.isEmpty()) {
            throw new ApplicationException(UserExceptionType.INCORRECT_USERNAME_OR_PASSWORD);
        }

        return user.get();
    }


}
