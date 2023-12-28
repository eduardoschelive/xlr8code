package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
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

    @Nested
    class CreationTests {

        @Test
        void it_should_create_user() {
            var user = buildUser("test", "test@test.com");

            var createdUser = userService.create(user);

            assertNotNull(createdUser);
        }

        @Test
        void it_should_have_unique_username() {
            var originalUser = buildUser("testUniqueUsername", "testUniqueUsername@test.com");
            var user = buildUser("testUniqueUsername", "testUniqueUsernameEmail@test.com");

            userService.create(originalUser);

            assertThrows(UsernameAlreadyTakenException.class, () -> userService.create(user));
        }

        @Test
        void it_should_have_unique_email() {
            var originalUser = buildUser("testUniqueEmail", "testUniqueEmail@test.com");
            var user = buildUser("testUniqueEmailUsername", "testUniqueEmail@test.com");

            userService.create(originalUser);

            assertThrows(EmailAlreadyInUseException.class, () -> userService.create(user));
        }

        @Test
        void it_should_encode_password() {
            var user = buildUser("testEncodePassword", "testEncodePassword@test.com");
            var userPassword = user.getPassword();

            var encodedPasswordUser = userService.create(user);

            assertNotEquals(userPassword, encodedPasswordUser.getPassword());
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
                .password("test")
                .metadata(userMetadata)
                .build();

        userMetadata.setUser(user);

        return user;
    }

}