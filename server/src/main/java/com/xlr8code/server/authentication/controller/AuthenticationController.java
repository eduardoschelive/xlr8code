package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.utils.SessionCookieUtils;
import com.xlr8code.server.common.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Authentication.BASE_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDTO signUpBodyDTO) {
        var newUser = this.authenticationService.signUp(signUpBodyDTO);

        var createdUserURI = URI.create(Endpoint.User.BASE_PATH + "/" + newUser.getId());

        return ResponseEntity.created(createdUserURI).build();
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<Void> signIn(@RequestBody @Valid SignInDTO signInRequestDTO) {
        var sessionToken = this.authenticationService.signIn(signInRequestDTO);
        var duration = signInRequestDTO.rememberMe() ? this.authenticationService.getSessionDuration() : -1;
        var cookie = SessionCookieUtils.createSessionToken(sessionToken, duration);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> signOut(@CookieValue(SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME) String sessionToken) {
        this.authenticationService.signOut(sessionToken);

        var invalidateSessionCookie = SessionCookieUtils.createSessionToken("", 0);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, invalidateSessionCookie).build();
    }

    @PostMapping(Endpoint.Authentication.REFRESH_SESSION)
    public ResponseEntity<Void> refreshSession(@CookieValue(SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME) String sessionToken) {
        var refreshedSessionToken = this.authenticationService.refreshSession(sessionToken);
        var cookie = SessionCookieUtils.createSessionToken(refreshedSessionToken, this.authenticationService.getSessionDuration());

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @PostMapping(Endpoint.Authentication.ACTIVATE_USER)
    public ResponseEntity<Void> activateUser(@RequestBody @Valid ActivateUserDTO activateUserDTO) {
        var code = activateUserDTO.code();
        var sessionToken = this.authenticationService.activateUser(code);

        // expire when user closes the browser
        var cookie = SessionCookieUtils.createSessionToken(sessionToken, -1);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @PostMapping(Endpoint.Authentication.RESEND_ACTIVATION_CODE)
    public ResponseEntity<Void> resendActivationCode(@RequestBody @Valid ResendCodeDTO resendCodeRequestDTO) {
        this.authenticationService.resendActivationCode(resendCodeRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.FORGOT_PASSWORD)
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDTO forgotPasswordRequestDTO) {
        this.authenticationService.forgotPassword(forgotPasswordRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.RESET_PASSWORD)
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordRequestDTO) {
        this.authenticationService.resetPassword(resetPasswordRequestDTO);

        return ResponseEntity.noContent().build();
    }

}
