package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * @param login the username identifying the user whose data is required. Cannot be null.
     * @return a fully populated user record (never null)
     * @throws IncorrectUsernameOrPasswordException if the username or currentPassword is incorrect
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        return this.userService.findByLogin(login);
    }

    /**
     * @param id the id identifying the user whose data is required. Cannot be null.
     * @return a fully populated user record (never null)
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(UUID id) {
        return this.userService.findByUUID(id);
    }

}
