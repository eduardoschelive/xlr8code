package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.InvalidRefreshSessionTokenException;
import com.xlr8code.server.authentication.exception.SessionExpiredException;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserSessionServiceTest {

    public static final String INACTIVE_USERNAME = "test-inactive";
    public static final String INACTIVE_MAIL = "test-inactive@test.com";

    public static final String ACTIVE_USERNAME = "test-active";
    public static final String ACTIVE_MAIL = "test-active@test.com";
    public static final String SESSION_TOKEN_EXAMPLE = "test-token";
    public static User inactiveUser;
    public static User activeUser;
    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var createUserDTO = UserTestUtils.buildCreateUserDTO(INACTIVE_USERNAME, INACTIVE_MAIL, "test");
        inactiveUser = userService.create(createUserDTO);

        var createActiveUserDTO = UserTestUtils.buildCreateUserDTO(ACTIVE_USERNAME, ACTIVE_MAIL, "test", true);
        activeUser = userService.create(createActiveUserDTO);
    }

    @AfterAll
    static void tearDownAll(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userSessionRepository.deleteAll();
    }


    @Test
    void it_should_generate_user_session() {
        var userSessionToken = userSessionService.generate(inactiveUser);
        assertNotNull(userSessionToken);
    }

    @Test
    void it_should_validate_session_token() {
        var userSessionToken = userSessionService.generate(activeUser);

        var validatedUserSession = userSessionService.validateSessionToken(userSessionToken);

        assertNotNull(validatedUserSession);
    }

    @Test
    void it_should_not_validate_session_when_user_inactive() {
        var userSessionToken = userSessionService.generate(inactiveUser);

        Executable executable = () -> {
            userSessionService.validateSessionToken(userSessionToken);
        };

        assertThrows(AccountNotActivatedException.class, executable);
    }

    @Test
    void it_should_not_validate_session_when_expired() {
        var userSession = userSessionService.create(activeUser, SESSION_TOKEN_EXAMPLE);

        var newExpireDate = userSession.getExpiresAt().toInstant().minus(userSessionService.getSessionDuration() * 10, ChronoUnit.DAYS);
        userSession.setExpiresAt(Date.from(newExpireDate));

        userSessionRepository.save(userSession);

        Executable executable = () -> {
            userSessionService.validateSessionToken(SESSION_TOKEN_EXAMPLE);
        };

        assertThrows(SessionExpiredException.class, executable);
    }

    @Test
    void it_should_not_validate_invalid_session_token() {
        Executable executable = () -> {
            userSessionService.validateSessionToken("invalid");
        };

        assertThrows(InvalidRefreshSessionTokenException.class, executable);
    }

    @Test
    void it_should_refresh_session() {
        var userSession = userSessionService.create(activeUser, SESSION_TOKEN_EXAMPLE);
        var expirationDate = userSession.getExpiresAt();

        var refreshedUserSession = userSessionService.refresh(userSession);
        var newExpirationDate = refreshedUserSession.getExpiresAt();

        assertNotNull(refreshedUserSession);
        assertTrue(newExpirationDate.after(expirationDate));
    }

    @Test
    void it_should_end_specific_session() {
        var userSession = userSessionService.create(activeUser, SESSION_TOKEN_EXAMPLE);

        userSessionService.end(SESSION_TOKEN_EXAMPLE);
        var userSessionOptional = userSessionRepository.findBySessionToken(SESSION_TOKEN_EXAMPLE);

        assertTrue(userSessionOptional.isEmpty());
    }

    @Test
    void it_should_end_all_sessions_from_user() {
        final var MAX_SESSIONS = 10;

        for (int i = 0; i < MAX_SESSIONS; i++) {
            userSessionService.generate(activeUser);
        }

        userSessionService.endAllFromUser(activeUser);

        var userSessions = userSessionRepository.findAllByUser(activeUser);

        assertEquals(0, userSessions.size());
    }

}