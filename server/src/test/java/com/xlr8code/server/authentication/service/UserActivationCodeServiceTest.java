package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserCodeType;
import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.ExpiredActivationCodeException;
import com.xlr8code.server.authentication.exception.InvalidActivationCodeException;
import com.xlr8code.server.authentication.repository.UserCodeRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserActivationCodeServiceTest {

    private static User inactiveUser;
    private static User activeUser;
    @Autowired
    private UserActivationCodeService userActivationCodeService;
    @Autowired
    private UserCodeRepository userCodeRepository;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var createInactiveUserDTO = UserTestUtils.buildCreateUserDTO("test", "test@test.com", "test", false);
        inactiveUser = userService.create(createInactiveUserDTO);

        var createActiveUserDTO = UserTestUtils.buildCreateUserDTO("test2", "test2@test.com", "test2", true);
        activeUser = userService.create(createActiveUserDTO);
    }

    @AfterAll
    static void tearDownAll(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    void it_should_generate_activation_code() {
        var activationCode = this.userActivationCodeService.generate(inactiveUser);

        assertNotNull(activationCode);
    }

    @Test
    void it_should_validate_activation_codes() {
        var activationCode = this.userActivationCodeService.generate(inactiveUser);

        var decodedActivationCode = this.userActivationCodeService.validate(activationCode.getCode());

        assertNotNull(decodedActivationCode);
    }

    @Test
    void it_should_reject_invalid_activation_codes() {
        Executable execute = () -> this.userActivationCodeService.validate("invalid");

        assertThrows(InvalidActivationCodeException.class, execute);
    }

    @Test
    void it_should_remove_all_user_codes() {
        final var MAX_CODES = 10;

        for (int i = 0; i < MAX_CODES; i++) {
            this.userActivationCodeService.generate(inactiveUser);
        }

        this.userActivationCodeService.removeAllFromUser(inactiveUser);

        var codes = this.userCodeRepository.findAllByUserAndCodeType(inactiveUser, UserCodeType.ACTIVATION);

        assertEquals(0, codes.size());
    }

    @Test
    void it_should_not_activate_already_activated_users() {
        Executable execute = () -> this.userActivationCodeService.generate(activeUser);

        assertThrows(AccountAlreadyActivatedException.class, execute);
    }

    @Test
    void it_should_not_validate_expired_activation_codes() {
        var activationCode = this.userActivationCodeService.generate(inactiveUser);

        activationCode.setExpiresAt(DateTimeUtils.calculateExpiresAt(-1, ChronoUnit.DAYS));

        this.userCodeRepository.save(activationCode);

        Executable execute = () -> this.userActivationCodeService.validate(activationCode.getCode());

        assertThrows(ExpiredActivationCodeException.class, execute);
    }

}