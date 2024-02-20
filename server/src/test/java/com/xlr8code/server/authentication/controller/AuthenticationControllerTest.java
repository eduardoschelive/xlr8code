package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.exception.InvalidRefreshSessionTokenException;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.utils.UserTestUtils;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.xlr8code.server.utils.JsonTestUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthenticationControllerTest {

    private static final String USERNAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";

    private static final String SESSION_TOKEN_COOKIE_NAME = "session_token";

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Nested
    class SignUpTest {

        @Test
        void should_sign_up() throws Exception {

            var expectedUser = UserTestUtils.buildUser(USERNAME, EMAIL, PASSWORD, passwordEncoder);
            var signUpDTO = buildSignUpDTO(EMAIL);

            when(authenticationService.signUp(signUpDTO)).thenReturn(expectedUser);

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.SIGN_UP)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(signUpDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));

        }

        @Test
        void should_return_bad_request_when_email_is_invalid() throws Exception {
            var signUpDTO = buildSignUpDTO("invalid");

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.SIGN_UP)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(signUpDTO)))
                    .andExpect(status().isBadRequest());
        }

        private SignUpDTO buildSignUpDTO(String email) {
            return new SignUpDTO(
                    USERNAME,
                    email,
                    PASSWORD,
                    Theme.SYSTEM,
                    Language.AMERICAN_ENGLISH,
                    null
            );
        }
    }

    @Nested
    class SignInTests {

        @Test
        void should_sign_in() throws Exception {
            var signInDTO = new SignInDTO(USERNAME, PASSWORD);

            when(authenticationService.signIn(signInDTO)).thenReturn(new AuthResultDTO(
                    "token",
                    "sessionToken"
            ));

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.SIGN_IN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(signInDTO)))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(SESSION_TOKEN_COOKIE_NAME));

        }

    }

    @Nested
    class SignOutTests {

        @Test
        void should_sign_out() throws Exception {

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.SIGN_OUT).cookie(new Cookie("session_token", "sessionToken")))
                    .andExpect(status().isNoContent())
                    .andExpect(cookie().exists(SESSION_TOKEN_COOKIE_NAME))
                    .andExpect(cookie().maxAge(SESSION_TOKEN_COOKIE_NAME, 0));

        }

        @Test
        void should_return_bad_request_when_session_token_is_missing() throws Exception {

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.SIGN_OUT))
                    .andExpect(status().isBadRequest());

        }

    }

    @Nested
    class RefreshSessionTests {

        private final String SESSION_TOKEN = UUID.randomUUID().toString();

        @Test
        void should_refresh_session() throws Exception {
            var refreshedSessionAuthResult = new AuthResultDTO("token", "sessionToken");
            var sessionCookie = new Cookie(SESSION_TOKEN_COOKIE_NAME, SESSION_TOKEN);

            when(authenticationService.refreshSession(SESSION_TOKEN)).thenReturn(refreshedSessionAuthResult);

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.REFRESH_SESSION).cookie(sessionCookie))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(SESSION_TOKEN_COOKIE_NAME));

        }

        @Test
        void should_return_bad_request_when_session_token_is_missing() throws Exception {
            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.REFRESH_SESSION))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_bad_request_when_session_token_is_invalid() throws Exception {
            var invalidSessionCookie = new Cookie(SESSION_TOKEN_COOKIE_NAME, "invalid");

            when(authenticationService.refreshSession("invalid")).thenThrow(new InvalidRefreshSessionTokenException());

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.REFRESH_SESSION).cookie(invalidSessionCookie))
                    .andExpect(status().isUnauthorized());

        }

    }

    @Nested
    class ActivationTests {

        public static final String ACTIVATION_URL_TEMPLATE = Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.ACTIVATE_USER;
        public static final String RESEND_ACTIVATION_CODE_URL_TEMPLATE = Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.RESEND_ACTIVATION_CODE;

        @Test
        void should_activate_user() throws Exception {
            var activationCode = "activationCode";

            mockMvc.perform(get(ACTIVATION_URL_TEMPLATE).param("code", activationCode))
                    .andExpect(status().isNoContent());
        }

        @Test
        void should_return_bad_request_when_activation_code_is_missing() throws Exception {
            mockMvc.perform(get(ACTIVATION_URL_TEMPLATE))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_resend_activation_code() throws Exception {
            var resendCodeDTO = new ResendCodeDTO(EMAIL);

            mockMvc.perform(post(RESEND_ACTIVATION_CODE_URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(resendCodeDTO)))
                    .andExpect(status().isNoContent());
        }

        @Test
        void should_return_bad_request_when_not_sending_data() throws Exception {
            mockMvc.perform(post(RESEND_ACTIVATION_CODE_URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class ForgotPasswordTests {

        private static final String FORGOT_PASSWORD_URL_TEMPLATE = Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.FORGOT_PASSWORD;

        @Test
        void should_send_forgot_password_email() throws Exception {
            var resendCodeDTO = new ForgotPasswordDTO(EMAIL);

            mockMvc.perform(post(FORGOT_PASSWORD_URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(resendCodeDTO)))
                    .andExpect(status().isNoContent());
        }

        @Test
        void should_return_bad_request_when_not_sending_data() throws Exception {
            mockMvc.perform(post(FORGOT_PASSWORD_URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_reset_password() throws Exception {
            var resetPasswordDTO = new ResetPasswordDTO("code", "newPassword", "newPassword");

            mockMvc.perform(post(Endpoint.Authentication.BASE_PATH + Endpoint.Authentication.RESET_PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(resetPasswordDTO)))
                    .andExpect(status().isNoContent());
        }

    }

}