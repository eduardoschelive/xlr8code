package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserCodeType;
import com.xlr8code.server.authentication.exception.ExpiredPasswordResetCodeException;
import com.xlr8code.server.authentication.exception.InvalidPasswordResetCodeException;
import com.xlr8code.server.authentication.repository.UserCodeRepository;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserPasswordResetCodeServiceTest {

    public static final String USERNAME = "test";
    public static final String MAIL = "test@test.com";

    public static User user;

    @Autowired
    private UserPasswordResetCodeService userPasswordResetCodeService;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var createUserDTO = UserTestUtils.buildCreateUserDTO(USERNAME, MAIL, "test");
        user = userService.create(createUserDTO);
    }

    @AfterAll
    static void tearDownAll(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    void it_should_generate_reset_code() {
        var userPasswordResetCode = userPasswordResetCodeService.generate(user);
        assertNotNull(userPasswordResetCode);
    }

    @Test
    void it_should_validate_reset_code() {
        var userPasswordResetCode = userPasswordResetCodeService.generate(user);
        var decodedUserPasswordResetCode = userPasswordResetCodeService.validate(userPasswordResetCode.getCode());
        assertNotNull(decodedUserPasswordResetCode);
    }

    @Test
    void it_should_not_validate_invalid_reset_code() {
        var invalidCode = UUID.randomUUID().toString();

        Executable executable = () -> userPasswordResetCodeService.validate(invalidCode);

        assertThrows(InvalidPasswordResetCodeException.class, executable);
    }

    @Test
    void it_should_not_validate_expired_reset_code() {
        var userPasswordResetCode = userPasswordResetCodeService.generate(user);
        var expiredDate = userPasswordResetCode.getExpiresAt().minus(1, ChronoUnit.DAYS);

        userPasswordResetCode.setExpiresAt(expiredDate);

        userCodeRepository.save(userPasswordResetCode);

        Executable executable = () -> userPasswordResetCodeService.validate(userPasswordResetCode.getCode());

        assertThrows(ExpiredPasswordResetCodeException.class, executable);
    }

    @Test
    void it_should_remove_all_from_user() {
        final var MAX_CODES = 10;

        for (int i = 0; i < MAX_CODES; i++) {
            userPasswordResetCodeService.generate(user);
        }

        userPasswordResetCodeService.removeAllFromUser(user);
        var codes = userCodeRepository.findAllByUserAndCodeType(user, UserCodeType.PASSWORD_RESET);
        assertEquals(0, codes.size());
    }
}