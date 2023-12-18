package com.xlr8code.server.user.service;

import com.xlr8code.server.common.exception.ApplicationError;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserErrorCode;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String password) throws ApplicationError {
        var userOptional = this.userRepository.findUserByUsernameOrEmailIgnoreCase(username, email);

        if (userOptional.isPresent()) {
            throw new ApplicationError(UserErrorCode.USER_ALREADY_EXISTS);
        }

        var passwordHash = this.passwordEncoder.encode(password);
        var roles = Set.of(UserRole.MEMBER.toRole());
        var user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .roles(roles)
                .build();

        return this.userRepository.save(user);
    }

    public User findByLogin(String login) {
        return this.userRepository.findUserByLogin(login);
    }

}
