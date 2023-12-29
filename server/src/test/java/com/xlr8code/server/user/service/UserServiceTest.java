package com.xlr8code.server.user.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "test";

    private User defaultUser;

    @BeforeEach
    void setUp() {
        var user = buildUser(DEFAULT_USERNAME, DEFAULT_EMAIL);
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
            var user = buildUser(DEFAULT_USERNAME, "not_test@test.com");

            assertThrows(UsernameAlreadyTakenException.class, () -> userService.create(user));
        }

        @Test
        void it_should_have_unique_email() {
            var user = buildUser("not_test", DEFAULT_EMAIL);

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
                var nonExistingUsername = "not_test";

                assertThrows(IncorrectUsernameOrPasswordException.class, () -> userService.findByLogin(nonExistingUsername));
            }

            @Test
            void it_should_not_find_user_by_email() {
                var nonExistingEmail = "not_test@nottest.com";

                assertThrows(IncorrectUsernameOrPasswordException.class, () -> userService.findByLogin(nonExistingEmail));
            }


            @Test
            void it_should_find_by_uuid() {
                var id = defaultUser.getId();

                var user = userService.findById(id);

                assertNotNull(user);
            }

            @Test
            void it_should_find_by_uuid_string() {
                var idString = defaultUser.getId().toString();

                var user = userService.findById(idString);

                assertNotNull(user);
            }

            @Test
            void it_should_not_find_by_invalid_uuid() {
                var id = "not_a_uuid";

                assertThrows(UserNotFoundException.class, () -> userService.findById(id));
            }

    }

    private User buildUser(String username, String email) {
        var userMetadata = UserMetadata.builder()
                .languagePreference(Language.AMERICAN_ENGLISH)
                .themePreference(Theme.LIGHT)
                .build();

        var user = User.builder()
                .username(username)
                .email(email)
                .password(DEFAULT_PASSWORD)
                .metadata(userMetadata)
                .build();

        userMetadata.setUser(user);

        return user;
    }

}