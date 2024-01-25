package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.event.OnCreateUserEvent;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSessionService userSessionService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * @param userCreateDTO User to be created
     *                      <p>
     *                      This method creates a user and saves it to the database. Also publishes an event to send an activation email.
     *                      </p>
     * @throws UsernameAlreadyTakenException if the username is already taken
     * @throws EmailAlreadyInUseException    if the email is already in use
     * @see OnCreateUserEvent
     */
    @Transactional
    public User create(CreateUserDTO userCreateDTO) {
        if (this.isUsernameTaken(userCreateDTO.username()))
            throw new UsernameAlreadyTakenException();

        if (this.isEmailInUse(userCreateDTO.email()))
            throw new EmailAlreadyInUseException();


        var user = userCreateDTO.toUserWithMetadata();

        this.encodeUserPassword(user);

        var newUser = this.userRepository.save(user);

        this.applicationEventPublisher.publishEvent(new OnCreateUserEvent(newUser));

        return newUser;
    }


    /**
     * @param login Username or email of the user
     * @return {@link User} with the given username or email
     * @throws IncorrectUsernameOrPasswordException if the username or password is incorrect
     */
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login)
                .orElseThrow(IncorrectUsernameOrPasswordException::new);
    }

    /**
     * @param user                    User to have the password changed
     * @param newPassword             New password
     * @param newPasswordConfirmation New password confirmation
     *                                <p>
     *                                This method changes the password of the user and ends all sessions of the user.
     *                                </p>
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
     *             <p>
     *             This method activates the user and saves it to the database.
     *             </p>
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
    public boolean isUsernameTaken(String username) {
        return this.userRepository.findUserByUsernameIgnoreCase(username).isPresent();
    }

    /**
     * @param email Email to be checked
     * @return true if the email is in use, false otherwise
     */
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

    /**
     * @param uuid UUID of the user
     * @return User with the given id
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public User findByUUID(UUID uuid) {
        return this.userRepository.findById(uuid)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * @param id UUID of the user
     * @return UserDTO of the user
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDTO findByUUID(String id) {
        var uuid = UUIDUtils.convertFromString(id).orElseThrow(UserNotFoundException::new);

        var user = this.userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        return UserDTO.fromUser(user);
    }

    @Transactional
    public void deleteByUUID(String id) {
        var uuid = UUIDUtils.convertFromString(id).orElseThrow(UserNotFoundException::new);

        if (!this.userRepository.existsById(uuid)) {
            throw new UserNotFoundException();
        }

        this.userRepository.deleteById(uuid);
    }

}
