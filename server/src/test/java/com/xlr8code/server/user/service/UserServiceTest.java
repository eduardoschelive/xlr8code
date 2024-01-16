package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "test";

    private static final String FALSE_USERNAME = "not_test";
    private static final String FALSE_EMAIL = "not_test@nottest.com";

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

                assertNotNull(user);
                assertEquals(user, defaultUser);
            }

            @Test
            void it_should_find_user_by_email() {
                var user = userService.findByLogin(DEFAULT_EMAIL);

                assertNotNull(user);
                assertEquals(user, defaultUser);
            }

            @Test
            void it_should_not_find_user_by_username() {
                assertThrows(IncorrectUsernameOrPasswordException.class, () -> userService.findByLogin(FALSE_USERNAME));
            }

            @Test
            void it_should_not_find_user_by_email() {
                assertThrows(IncorrectUsernameOrPasswordException.class, () -> userService.findByLogin(FALSE_EMAIL));
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

    }
}