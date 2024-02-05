package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthenticationServiceTest {

    private static final String ACTIVE_USERNAME = "test";
    private static final String ACTIVE_EMAIL = "test@test.com";

    private static final String INACTIVE_USERNAME = "test-inactive";
    private static final String INACTIVE_EMAIL = "test-inactive@test.com";

    private static final String PASSWORD = "password";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserActivationCodeService userActivationCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPasswordResetCodeService userPasswordResetCodeService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    private User activeUser;

    private User inactiveUser;

    private static SignUpDTO getSignUpDTO() {
        return new SignUpDTO(
                "SIGN_UP_TEST",
                "SIGN_UP_TEST@test.com",
                PASSWORD,
                Theme.SYSTEM,
                Language.AMERICAN_ENGLISH,
                null
        );
    }

    @BeforeEach
    void setUp() {
        var createActiveUserDTO = UserTestUtils.buildCreateUserDTO(ACTIVE_USERNAME, ACTIVE_EMAIL, PASSWORD, true);
        this.activeUser = userService.create(createActiveUserDTO);

        var createInactiveUserDTO = UserTestUtils.buildCreateUserDTO(INACTIVE_USERNAME, INACTIVE_EMAIL, PASSWORD + "2");
        this.inactiveUser = userService.create(createInactiveUserDTO);
    }

    @AfterEach
    void tearDown(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    void it_should_sign_up() {
        var signUpDTO = getSignUpDTO();

        var user = authenticationService.signUp(signUpDTO);

        assertNotNull(user);
    }

    @Test
    void it_should_not_sign_in_if_inactive() {
        var signInDTO = new SignInDTO(INACTIVE_USERNAME, PASSWORD);

        Executable executable = () -> authenticationService.signIn(signInDTO);

        assertThrows(AccountNotActivatedException.class, executable);
    }

    @Test
    void it_should_sign_in() {
        var signInDTO = new SignInDTO(ACTIVE_USERNAME, PASSWORD);
        var authResultDTO = authenticationService.signIn(signInDTO);

        assertNotNull(authResultDTO);
    }

    @Test
    void it_should_sign_out() {
        var signInDTO = new SignInDTO(ACTIVE_USERNAME, PASSWORD);
        var authResultDTO = authenticationService.signIn(signInDTO);
        var sessionToken = authResultDTO.sessionToken();

        authenticationService.signOut(sessionToken);

        var user = userService.findByUUID(activeUser.getId());

        var userSession = userSessionRepository.findAllByUser(user);

        assertTrue(userSession.isEmpty());
    }

    @Test
    void it_should_refresh_session() {
        var signInDTO = new SignInDTO(ACTIVE_USERNAME, PASSWORD);
        var authResultDTO = authenticationService.signIn(signInDTO);
        var sessionToken = authResultDTO.sessionToken();

        var newAuthResultDTO = authenticationService.refreshSession(sessionToken);

        assertNotNull(newAuthResultDTO);
    }

    @Test
    void it_should_activate_the_user() {
        var activationCode = userActivationCodeService.generate(inactiveUser);

        authenticationService.activateUser(activationCode.getCode());

        var activatedUser = userService.findByUUID(inactiveUser.getId());

        assertTrue(activatedUser.isActive());
    }

    @Test
    void it_should_resend_activation_code() {
        var resendActivationCodeDTO = new ResendCodeDTO(inactiveUser.getUsername());

        assertDoesNotThrow(() -> authenticationService.resendActivationCode(resendActivationCodeDTO));
    }

    @Test
    void it_should_not_resend_activation_code_if_user_is_active() {
        var resendActivationCodeDTO = new ResendCodeDTO(activeUser.getUsername());

        Executable executable = () -> authenticationService.resendActivationCode(resendActivationCodeDTO);

        assertThrows(AccountAlreadyActivatedException.class, executable);
    }

    @Test
    void it_should_forgot_password() {
        var forgotPasswordDTO = new ForgotPasswordDTO(ACTIVE_EMAIL);

        assertDoesNotThrow(() -> authenticationService.forgotPassword(forgotPasswordDTO));
    }

    @Test
    void it_should_not_forgot_password_with_invalid_credentials() {
        var forgotPasswordDTO = new ForgotPasswordDTO("invalid");

        Executable executable = () -> authenticationService.forgotPassword(forgotPasswordDTO);

        assertThrows(IncorrectUsernameOrPasswordException.class, executable);
    }

    @Test
    void it_should_reset_password() {
        var passwordResetCode = this.userPasswordResetCodeService.generate(activeUser);

        var newPassword = "newPassword";
        var newPasswordConfirmation = "newPassword";

        var resetPasswordDTO = new ResetPasswordDTO(
                passwordResetCode.getCode(),
                newPassword,
                newPasswordConfirmation
        );

        assertDoesNotThrow(() -> authenticationService.resetPassword(resetPasswordDTO));
    }

    @Test
    void it_should_only_allow_matching_passwords() {
        var passwordResetCode = this.userPasswordResetCodeService.generate(activeUser);

        var newPassword = "newPassword";
        var newPasswordConfirmation = "newPassword2";

        var resetPasswordDTO = new ResetPasswordDTO(
                passwordResetCode.getCode(),
                newPassword,
                newPasswordConfirmation
        );

        Executable executable = () -> authenticationService.resetPassword(resetPasswordDTO);

        assertThrows(PasswordMatchException.class, executable);
    }

}