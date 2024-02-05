package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UpdateUserDTO;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.event.OnCreateUserEvent;
import com.xlr8code.server.user.exception.*;
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
        this.validateUsername(userCreateDTO.username());
        this.validateEmail(userCreateDTO.email());

        var user = userCreateDTO.toUserWithMetadata();

        this.encodeUserPassword(user);

        var newUser = this.userRepository.save(user);

        this.applicationEventPublisher.publishEvent(new OnCreateUserEvent(newUser));

        return newUser;
    }

    /**
     * @param login Username or email of the user
     * @return {@link User} with the given username or email
     * @throws IncorrectUsernameOrPasswordException if the username or currentPassword is incorrect
     */
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login)
                .orElseThrow(IncorrectUsernameOrPasswordException::new);
    }

    /**
     * @param user                    User to have the currentPassword changed
     * @param newPassword             New currentPassword
     * @param newPasswordConfirmation New currentPassword confirmation
     *                                <p>
     *                                This method changes the currentPassword of the user and ends all sessions of the user.
     *                                </p>
     * @throws PasswordMatchException if the new currentPassword and the new currentPassword confirmation do not match
     */
    @Transactional
    public void changePassword(User user, String newPassword, String newPasswordConfirmation) {
        this.validatePasswordChange(newPassword, newPasswordConfirmation);

        this.changeUserPassword(user, newPassword);

        this.userRepository.save(user);
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
     * @param uuidString UUID of the user
     * @return {@link UserDTO} of the user
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDTO findByUUID(String uuidString) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        var user = this.findByUUID(uuid);

        return UserDTO.fromUser(user);
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
     * @param uuidString UUID of the user
     * @throws UserNotFoundException if the user is not found or if uuid is not valid
     */
    @Transactional
    public void deleteByUUID(String uuidString) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        this.deleteByUUID(uuid);
    }

    /**
     * @param uuid {@link UUID} of the user
     * @throws UserNotFoundException if the user is not found or if uuid is not valid
     */
    @Transactional
    public void deleteByUUID(UUID uuid) {
        if (!this.userRepository.existsById(uuid)) {
            throw new UserNotFoundException();
        }
        this.userRepository.deleteById(uuid);
    }

    /**
     * @param uuid          UUID of the user
     * @param updateUserDTO {@link UpdateUserDTO} of the user
     * @return {@link UserDTO} of the updated user
     * @throws UserNotFoundException         if the user is not found
     * @throws UsernameAlreadyTakenException if the username is already taken
     * @throws EmailAlreadyInUseException    if the email is already in use
     * @throws IncorrectOldPasswordException          if the current currentPassword is incorrect
     * @throws InvalidNewPasswordException   if the new currentPassword is invalid
     * @throws PasswordMatchException        if the new currentPassword and the new currentPassword confirmation do not match
     */
    @Transactional
    public UserDTO updateByUUID(UUID uuid, UpdateUserDTO updateUserDTO) {
        var user = this.findByUUID(uuid);

        if (shouldChangeUsername(user.getUsername(), updateUserDTO.username())) {
            this.changeUsername(user, updateUserDTO.username());
        }

        if (shouldChangeEmail(user.getEmail(), updateUserDTO.email())) {
            this.changeEmail(user, updateUserDTO.email());
        }

        boolean shouldChangePassword = shouldChangePassword(user.getPassword(),
                updateUserDTO.currentPassword(),
                updateUserDTO.newPassword());

        if (shouldChangePassword) {
            this.changeUserPassword(user, updateUserDTO.newPassword());
        }

        var updatedUser = this.userRepository.save(user);

        return UserDTO.fromUser(updatedUser);
    }

    /**
     * @param passwordHash    Hash of the current currentPassword
     * @param currentPassword Current currentPassword
     * @return true if the current currentPassword is correct, false otherwise
     * @throws IncorrectOldPasswordException if the current currentPassword is incorrect
     */
    private boolean shouldChangePassword(String passwordHash, String currentPassword, String newPassword) {
        if (currentPassword == null) {
            return false;
        }

        if (newPassword == null) {
            throw new InvalidNewPasswordException();
        }

        if (!this.passwordEncoder.matches(currentPassword, passwordHash)) {
            throw new IncorrectOldPasswordException();
        }

        return true;
    }

    /**
     * @param uuidString    {@link UUID} of the user
     * @param updateUserDTO {@link UpdateUserDTO} of the user
     * @return {@link UserDTO} of the updated user
     */
    @Transactional
    public UserDTO updateByUUID(String uuidString, UpdateUserDTO updateUserDTO) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        return this.updateByUUID(uuid, updateUserDTO);
    }

    /**
     * @param currentUsername Current username of the user
     * @param newUsername     New username of the user
     * @return true if the username should be updated, false otherwise
     */
    private boolean shouldChangeUsername(String currentUsername, String newUsername) {
        return newUsername != null && !currentUsername.equals(newUsername);
    }

    /**
     * @param currentEmail Current email of the user
     * @param newEmail     New email of the user
     * @return true if the email should be updated, false otherwise
     */
    private boolean shouldChangeEmail(String currentEmail, String newEmail) {
        return newEmail != null && !currentEmail.equals(newEmail);
    }

    /**
     * @param user        User to have the username updated
     * @param newUsername New username
     * @throws UsernameAlreadyTakenException if the username is already taken
     */
    private void changeUsername(User user, String newUsername) {
        this.validateUsername(newUsername);
        user.setUsername(newUsername);
    }

    /**
     * @param user     User to have the email updated
     * @param newEmail New email
     * @throws EmailAlreadyInUseException if the email is already in use
     */
    private void changeEmail(User user, String newEmail) {
        this.validateEmail(newEmail);
        user.setEmail(newEmail);
    }

    /**
     * @param username Username to be validated
     * @throws UsernameAlreadyTakenException if the username is already taken
     */
    private void validateUsername(String username) {
        if (this.isUsernameTaken(username))
            throw new UsernameAlreadyTakenException();
    }

    /**
     * @param email Email to be validated
     * @throws EmailAlreadyInUseException if the email is already in use
     */
    private void validateEmail(String email) {
        if (this.isEmailInUse(email))
            throw new EmailAlreadyInUseException();
    }

    /**
     * @param user User to have the currentPassword encoded
     */
    private void encodeUserPassword(User user) {
        var password = user.getPassword();
        var passwordHash = this.passwordEncoder.encode(password);
        user.setPassword(passwordHash);
    }

    /**
     * @param user        User to have the currentPassword changed
     * @param newPassword New currentPassword
     * @throws PasswordMatchException if the new currentPassword and the new currentPassword confirmation do not match
     */
    private void changeUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        this.encodeUserPassword(user);

        this.userSessionService.endAllFromUser(user);
    }

    private void validatePasswordChange(String newPassword, String newPasswordConfirmation) {
        if (!newPassword.equals(newPasswordConfirmation)) {
            throw new PasswordMatchException();
        }
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

}
