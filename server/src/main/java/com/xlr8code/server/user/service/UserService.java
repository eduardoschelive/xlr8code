package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
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

    public User create(String username, String email, String password) {
        var userOptional = this.userRepository.findUserByUsernameOrEmailIgnoreCase(username, email);

        if(userOptional.isPresent()) {
            // TODO: Use a custom exception
            throw new IllegalArgumentException("User with username " + username + " already exists");
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
