package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.common.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Authentication.BASE_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String SESSION_TOKEN_COOKIE_NAME = "session_token";
    private final AuthenticationService authenticationService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDTO signUpBodyDTO) {
        var newUser = this.authenticationService.signUp(signUpBodyDTO);

        var createdUserURI = URI.create(Endpoint.User.BASE_PATH + "/" + newUser.getId());

        return ResponseEntity.created(createdUserURI).build();
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid SignInDTO signInRequestDTO) {
        var signInResultDTO = this.authenticationService.signIn(signInRequestDTO);
        return this.buildAuthResponse(signInResultDTO);
    }

    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> signOut(@CookieValue(SESSION_TOKEN_COOKIE_NAME) String sessionToken) {
        this.authenticationService.signOut(sessionToken);

        var invalidateSessionCookie = this.buildInvalidateSessionCookie();

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, invalidateSessionCookie).build();
    }

    @PostMapping(Endpoint.Authentication.REFRESH_SESSION)
    public ResponseEntity<TokenDTO> refreshSession(@CookieValue(SESSION_TOKEN_COOKIE_NAME) String sessionToken) {
        var refreshedSessionAuthResult = this.authenticationService.refreshSession(sessionToken);

        return this.buildAuthResponse(refreshedSessionAuthResult);
    }

    @GetMapping(Endpoint.Authentication.ACTIVATE_USER)
    public ResponseEntity<Void> activateUser(@RequestParam String code) {
        this.authenticationService.activateUser(code);

        return ResponseEntity.noContent().build();
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

    private ResponseEntity<TokenDTO> buildAuthResponse(AuthResultDTO authResultDTO) {
        var sessionToken = authResultDTO.sessionToken();

        var sessionCookie = this.buildSessionCookie(sessionToken, this.authenticationService.getSessionDuration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie)
                .body(new TokenDTO(authResultDTO.token()));
    }

    private String buildSessionCookie(String sessionToken, long maxAge) {
        var cookie = ResponseCookie.from(SESSION_TOKEN_COOKIE_NAME, sessionToken)
                .httpOnly(true)
                .maxAge(maxAge)
                .path("/")
                .build();

        return cookie.toString();
    }

    private String buildInvalidateSessionCookie() {
        return this.buildSessionCookie("", 0);
    }

}
