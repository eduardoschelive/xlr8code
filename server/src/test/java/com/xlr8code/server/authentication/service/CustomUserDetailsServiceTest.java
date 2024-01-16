package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CustomUserDetailsServiceTest {

    public static final String USERNAME = "test";
    public static final String MAIL = "test@test.com";

    public static UUID createdUserId;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var user = UserTestUtils.buildCreateUserDTO(USERNAME, MAIL, "test");

        var createdUser = userService.create(user);
        createdUserId = createdUser.getId();
    }

    @AfterAll
    static void tearDownAll(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    void it_should_load_user_by_username() {
        var user = this.customUserDetailsService.loadUserByUsername(USERNAME);

        assertNotNull(user);
    }

    @Test
    void it_should_load_user_by_email_as_username() {
        var user = this.customUserDetailsService.loadUserByUsername(MAIL);

        assertNotNull(user);
    }

    @Test
    void it_should_load_user_by_id() {
        var user = this.customUserDetailsService.loadUserById(createdUserId);

        assertNotNull(user);
    }

    @Test
    void it_should_throw_exception_when_user_not_found_by_id() {
        UUID uuid = UUID.randomUUID();

        Executable executable = () -> this.customUserDetailsService.loadUserById(uuid);

        assertThrows(UserNotFoundException.class, executable);
    }

    @Test
    void it_should_throw_exception_when_user_not_found_by_username() {
        Executable executable = () -> this.customUserDetailsService.loadUserByUsername("not_found");

        assertThrows(IncorrectUsernameOrPasswordException.class, executable);
    }

    @Test
    void it_should_throw_exception_when_user_not_found_by_email() {
        Executable executable = () -> this.customUserDetailsService.loadUserByUsername("not_test");

        assertThrows(IncorrectUsernameOrPasswordException.class, executable);
    }

}