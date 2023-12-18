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

    /**
     * @param user User to be created
     * <p>
     * This method creates a user and saves it to the database. Also publishes an event to send an activation email.
     * </p>
     * @throws UsernameAlreadyTakenException if the username is already taken
     * @throws EmailAlreadyInUseException    if the email is already in use
     * @see OnCreateUserEvent
     */
    @Transactional
    public void create(User user) {

        if (this.isUsernameTaken(user.getUsername()))
            throw new UsernameAlreadyTakenException();

        if (this.isEmailInUse(user.getEmail()))
            throw new EmailAlreadyInUseException();

        this.encodeUserPassword(user);
        this.userRepository.save(user);

        this.applicationEventPublisher.publishEvent(new OnCreateUserEvent(user));
    }


    /**
     * @param login Username or email of the user
     * @return Optional of the user
     */
    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login);
    }

    /**
     * @param user User to have the password changed
     * @param newPassword New password
     * @param newPasswordConfirmation New password confirmation
     * <p>
     * This method changes the password of the user and ends all sessions of the user.
     * </p>
     * @throws PasswordMatchException if the new password and the new password confirmation do not match
     */
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

    /**
     * @param user User to be activated
     * <p>
     * This method activates the user and saves it to the database.
     * </p>
     */
    @Transactional
    public void activate(User user) {
        user.activate();
        this.userRepository.save(user);
    }


    /**
     * @param username Username to be checked
     * @return true if the username is taken, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isUsernameTaken(String username) {
        return this.userRepository.findUserByUsernameIgnoreCase(username).isPresent();
    }

    /**
     * @param email Email to be checked
     * @return true if the email is in use, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isEmailInUse(String email) {
        return this.userRepository.findUserByEmailIgnoreCase(email).isPresent();
    }

    /**
     * @param user User to have the password encoded
     */
    private void encodeUserPassword(User user) {
        var password = user.getPassword();
        var passwordHash = this.passwordEncoder.encode(password);
        user.setPassword(passwordHash);
    }

}
