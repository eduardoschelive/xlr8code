package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.user.dto.UpdatePasswordDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.*;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "test";
    private static final String FALSE_USERNAME = "not_test";
    private static final String FALSE_EMAIL = "not_test@nottest.com";
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User defaultUser;

    @BeforeEach
    void setUp() {
        var user = UserTestUtils.buildCreateUserDTO(DEFAULT_USERNAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
        this.defaultUser = userService.create(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        this.defaultUser = null;
    }

    @Nested
    class CreationTests {

        @Test
        void it_should_create_user() {
            assertNotNull(defaultUser);
        }

        @Test
        void it_should_have_unique_username() {
            var user = UserTestUtils.buildCreateUserDTO(DEFAULT_USERNAME, FALSE_EMAIL, DEFAULT_PASSWORD);

            assertThrows(UsernameAlreadyTakenException.class, () -> userService.create(user));
        }

        @Test
        void it_should_have_unique_email() {
            var user = UserTestUtils.buildCreateUserDTO(FALSE_USERNAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);

            assertThrows(EmailAlreadyInUseException.class, () -> userService.create(user));
        }

        @Test
        void it_should_encode_password() {
            assertNotEquals(DEFAULT_PASSWORD, defaultUser.getPassword());
        }

    }

    @Nested
    class FindTests {

        @Test
        void it_should_find_user_by_username() {
            var user = userService.findByLogin(DEFAULT_USERNAME);

            assertTrue(user.isPresent());

            assertEquals(user.get(), defaultUser);
        }

        @Test
        void it_should_find_user_by_email() {
            var user = userService.findByLogin(DEFAULT_EMAIL);

            assertTrue(user.isPresent());

            assertEquals(user.get(), defaultUser);
        }


        @Test
        void it_should_find_by_uuid() {
            var id = defaultUser.getId();

            var user = userService.findByUUID(id);

            assertNotNull(user);
        }

        @Test
        void it_should_find_by_uuid_string() {
            var idString = defaultUser.getId().toString();

            var user = userService.findByUUID(idString);

            assertNotNull(user);
        }

        @Test
        void it_should_not_find_by_invalid_uuid() {
            var id = "not_a_uuid";

            assertThrows(UserNotFoundException.class, () -> userService.findByUUID(id));
        }

        @Test
        void it_should_find_all_users() {
            var users = userService.findAll(Specification.where(null), Pageable.unpaged());
            assertFalse(users.isEmpty());
        }

    }

    @Nested
    class ActivationTests {

        @Test
        void it_should_activate_user() {
            var uuid = defaultUser.getId();

            userService.activate(defaultUser);

            var user = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

            assertTrue(user.isActive());
        }

    }

    @Nested
    class ExistenceChecks {

        @Test
        void it_should_check_if_username_is_not_taken() {
            var isTaken = userService.isUsernameTaken(FALSE_USERNAME);

            assertFalse(isTaken);
        }

        @Test
        void it_should_check_if_username_is_taken() {
            var isTaken = userService.isUsernameTaken(DEFAULT_USERNAME);

            assertTrue(isTaken);
        }

        @Test
        void it_should_check_if_email_is_not_in_use() {
            var isTaken = userService.isEmailInUse(FALSE_EMAIL);

            assertFalse(isTaken);
        }

        @Test
        void it_should_check_if_email_is_in_use() {
            var isTaken = userService.isEmailInUse(DEFAULT_EMAIL);

            assertTrue(isTaken);
        }

    }

    @Nested
    class UpdateTests {

        private static final String NEW_PASSWORD = "new_password";

        @Test
        void it_should_not_change_non_matching_passwords() {
            var nonMatchingPassword = "not_matching_password";

            Executable changePassword = () -> userService.changePassword(defaultUser, nonMatchingPassword, NEW_PASSWORD);

            assertThrows(PasswordMatchException.class, changePassword);
        }

        @Test
        void it_should_change_password() {
            userService.changePassword(defaultUser, NEW_PASSWORD, NEW_PASSWORD);

            var user = userRepository.findById(defaultUser.getId()).orElseThrow(UserNotFoundException::new);

            assertTrue(passwordEncoder.matches(NEW_PASSWORD, user.getPassword()));
        }

        @Test
        void it_should_update_user() {
            var update = UserTestUtils.buildUpdateUserDTO("new_username", "new_email");

            var updatedUser = userService.updateByUUID(defaultUser.getId().toString(), update);

            assertEquals(update.username(), updatedUser.username());
            assertEquals(update.email(), updatedUser.email());
        }

        @Test
        void it_should_update_password() {
            var update = new UpdatePasswordDTO(DEFAULT_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
            var uuid = defaultUser.getId().toString();

            userService.updateUserPassword(uuid, update);

            var user = userRepository.findById(defaultUser.getId()).orElseThrow(UserNotFoundException::new);

            assertTrue(passwordEncoder.matches(NEW_PASSWORD, user.getPassword()));
        }

        @Test
        void it_should_not_update_password_when_new_passwords_do_not_match() {
            var update = new UpdatePasswordDTO(DEFAULT_PASSWORD, NEW_PASSWORD, "not_matching_password");
            var uuid = defaultUser.getId().toString();

            assertThrows(PasswordMatchException.class, () -> userService.updateUserPassword(uuid, update));
        }

        @Test
        void it_should_not_update_password_when_new_password_is_same_as_old_password() {
            var update = new UpdatePasswordDTO(DEFAULT_PASSWORD, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
            var uuid = defaultUser.getId().toString();

            assertThrows(PasswordAlreadyUsedException.class, () -> userService.updateUserPassword(uuid, update));
        }

        @Test
        void it_should_not_update_when_password_old_password_is_invalid() {
            var update = new UpdatePasswordDTO("invalid_old_password", "new_password", "new_password");
            var uuid = defaultUser.getId().toString();

            assertThrows(IncorrectOldPasswordException.class, () -> userService.updateUserPassword(uuid, update));
        }

    }

    @Nested
    class DeletionTests {

        @Test
        void it_should_delete_user() {
            var uuid = defaultUser.getId();

            userService.deleteByUUID(uuid.toString());

            var user = userRepository.findById(uuid);

            assertTrue(user.isEmpty());
        }

        @Test
        void it_should_not_delete_uuid_does_not_exists() {
            var uuid = "not_a_uuid";

            assertThrows(UserNotFoundException.class, () -> userService.deleteByUUID(uuid));
        }

    }


}