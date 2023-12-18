package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
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

    /**
     * @param login the username identifying the user whose data is required. Cannot be null.
     * @return a fully populated user record (never null)
     * @throws IncorrectUsernameOrPasswordException if the username or password is incorrect
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        return this.userService.findByLogin(login)
                .orElseThrow(IncorrectUsernameOrPasswordException::new);
    }

}
