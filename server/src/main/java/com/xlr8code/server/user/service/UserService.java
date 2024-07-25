package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.dto.UpdatePasswordDTO;
import com.xlr8code.server.user.dto.UpdateUserDTO;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.IncorrectOldPasswordException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSessionService userSessionService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Resource
    private UserService self;

    /**
     * @param userCreateDTO User to be created
     *                      <p>
     *                      This method creates a user and saves it to the database. Also publishes an event to send an activation email.
     *                      </p>
     * @throws UsernameAlreadyTakenException if the username is already taken
     * @throws EmailAlreadyInUseException    if the email is already in use
     */
    @Transactional
    public User create(CreateUserDTO userCreateDTO) {
        this.validateUsername(userCreateDTO.username());
        this.validateEmail(userCreateDTO.email());

        var user = userCreateDTO.toUser(passwordEncoder);

        return this.userRepository.save(user);
    }


    /**
     * @param specification {@link Specification} of {@link User}
     * @param pageable      {@link Pageable} of the query
     * @return {@link Page} of {@link UserDTO} with the given query parameters
     */
    public Page<UserDTO> findAll(Specification<User> specification, Pageable pageable) {
        var users = this.userRepository.findAll(specification, pageable);
        return users.map(UserDTO::from);
    }

    /**
     * @param login Username or email of the user
     * @return {@link User} with the given username or email
     * @throws IncorrectUsernameOrPasswordException if the username or currentPassword is incorrect
     */
    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login);
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
        self.changeUserPassword(user, newPassword);
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
        var user = self.findByUUID(uuid);

        return UserDTO.from(user);
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
        var uuid = UUIDUtils.convertFromString(uuidString)
                .orElseThrow(UserNotFoundException::new);
        self.deleteByUUID(uuid);
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
     * @param uuidString    {@link UUID} of the user
     * @param updateUserDTO {@link UpdateUserDTO} of the user
     * @return {@link UserDTO} of the updated user
     */
    @Transactional
    public UserDTO updateByUUID(String uuidString, UpdateUserDTO updateUserDTO) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        return self.updateByUUID(uuid, updateUserDTO);
    }

    /**
     * @param uuid          UUID of the user
     * @param updateUserDTO {@link UpdateUserDTO} of the user
     * @return {@link UserDTO} of the updated user
     */
    @Transactional
    public UserDTO updateByUUID(UUID uuid, UpdateUserDTO updateUserDTO) {
        var user = self.findByUUID(uuid);

        this.changeUsername(user, updateUserDTO.username());
        this.changeEmail(user, updateUserDTO.email());

        var updatedUser = this.userRepository.save(user);

        return UserDTO.from(updatedUser);
    }

    /**
     * @param userId            UUID of the user
     * @param updatePasswordDTO {@link UpdatePasswordDTO} of the user
     * @throws UserNotFoundException if the user is not found
     * @throws IncorrectOldPasswordException if the old currentPassword is incorrect
     * @throws PasswordMatchException if the new currentPassword and the new currentPassword confirmation do not match
     */
    @Transactional
    public void updateUserPassword(String userId, UpdatePasswordDTO updatePasswordDTO) {
        var uuid = UUIDUtils.convertFromString(userId).orElseThrow(UserNotFoundException::new);
        self.updateUserPassword(uuid, updatePasswordDTO);
    }

    /**
     * @param userId            UUID of the user
     * @param updatePasswordDTO {@link UpdatePasswordDTO} of the user
     */
    @Transactional
    public void updateUserPassword(UUID userId, UpdatePasswordDTO updatePasswordDTO) {
        var user = self.findByUUID(userId);

        this.validateOldPassword(user, updatePasswordDTO.oldPassword());

        this.validatePasswordChange(updatePasswordDTO.newPassword(), updatePasswordDTO.newPasswordConfirmation());

        self.changeUserPassword(user, updatePasswordDTO.newPassword());
    }

    /**
     * @param user User to have the currentPassword changed
     */
    @Transactional
    public void changeUserPassword(User user, String newPassword) {
        user.getUserPassword().setEncodedPassword(newPassword, passwordEncoder);
        this.userSessionService.endAllFromUser(user);
        this.userRepository.save(user);
    }

    /**
     * @param user        User to have the currentPassword validated
     * @param rawPassword rawPassword to be validated
     * @throws IncorrectOldPasswordException if the password is incorrect
     */
    private void validateOldPassword(User user, String rawPassword) {
        if (!user.getUserPassword().matches(rawPassword, passwordEncoder)) {
            throw new IncorrectOldPasswordException();
        }
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
     * @param newPassword             New currentPassword
     * @param newPasswordConfirmation New currentPassword confirmation
     * @throws PasswordMatchException if the new currentPassword and the new currentPassword confirmation do not match
     */
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
