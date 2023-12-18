package com.xlr8code.server.user.service;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserExceptionType;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String password) throws ApplicationException {
        var userOptional = this.userRepository.findUserByUsernameOrEmailIgnoreCase(username, email);

        if (userOptional.isPresent()) {
            throw new ApplicationException(UserExceptionType.USER_ALREADY_EXISTS);
        }

        var passwordHash = this.passwordEncoder.encode(password);
        var roles = Set.of(UserRole.getDefaultValue().toRole());
        var user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .roles(roles)
                .build();

        return this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login);
    }

}
