package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.event.OnCreateUserEvent;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSessionService userSessionService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void create(User user) {

        if (this.isUsernameTaken(user.getUsername())) {
            throw new UsernameAlreadyTakenException();
        }

        if (this.isEmailInUse(user.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        this.encodeUserPassword(user);
        this.userRepository.save(user);

        this.applicationEventPublisher.publishEvent(new OnCreateUserEvent(user));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login);
    }

    @Transactional
    public void changePassword(User user, String newPassword, String newPasswordConfirmation) {
        if (!newPassword.equals(newPasswordConfirmation)) {
            throw new PasswordMatchException();
        }

        var passwordHash = this.passwordEncoder.encode(newPassword);

        user.setPassword(passwordHash);

        this.userRepository.save(user);

        this.userSessionService.endAllFromUser(user);
    }

    @Transactional
    public void activate(User user) {
        user.activate();
        this.userRepository.save(user);
    }


    private boolean isUsernameTaken(String username) {
        return this.userRepository.findUserByUsernameIgnoreCase(username).isPresent();
    }

    private boolean isEmailInUse(String email) {
        return this.userRepository.findUserByEmailIgnoreCase(email).isPresent();
    }

    private void encodeUserPassword(User user) {
        var password = user.getPassword();
        var passwordHash = this.passwordEncoder.encode(password);
        user.setPassword(passwordHash);
    }

}
