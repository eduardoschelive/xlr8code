package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.ExpiredActivationCodeException;
import com.xlr8code.server.authentication.exception.InvalidActivationCodeException;
import com.xlr8code.server.authentication.repository.UserActivationCodeRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.CreateUserDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserActivationCodeServiceTest {

    @Autowired
    private UserActivationCodeService userActivationCodeService;

    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    private static User inactiveUser;

    private static User activeUser;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var createInactiveUser = buildUser("inactive-test", "inactive-test@test.com", false);
        inactiveUser = userService.create(createInactiveUser);

        var createActiveUser = buildUser("active-test", "active-test@test.com", true);
        activeUser = userService.create(createActiveUser);
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

        var codes = this.userActivationCodeRepository.findAllByUser(inactiveUser);

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
        
        this.userActivationCodeRepository.save(activationCode);

        Executable execute = () -> this.userActivationCodeService.validate(activationCode.getCode());

        assertThrows(ExpiredActivationCodeException.class, execute);
    }

    private static CreateUserDTO buildUser(String username, String email, boolean active) {
        return new CreateUserDTO(
                username,
                email,
                "test-activation-code",
                Set.of(UserRole.DEFAULT.toRole()),
                Theme.SYSTEM,
                Language.AMERICAN_ENGLISH,
                null,
                active
        );
    }

}