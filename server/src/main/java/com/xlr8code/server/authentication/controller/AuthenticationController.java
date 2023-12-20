package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

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
    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid SignInDTO signInRequestDTO) {
        var tokenPairDTO = this.authenticationService.signIn(signInRequestDTO);

        var sessionToken = this.buildSessionCookie(tokenPairDTO.userSession());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionToken.toString())
                .body(new TokenDTO(tokenPairDTO.token()));
    }

    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> signOut(@CookieValue("session_token") String sessionToken) {
        this.authenticationService.signOut(sessionToken);

        var invalidateSessionCookie = this.buildInvalidateSessionCookie().toString();

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, invalidateSessionCookie).build();
    }

    @PostMapping(Endpoint.Authentication.REFRESH_SESSION)
    public ResponseEntity<TokenDTO> refreshSession(@CookieValue("session_token") String sessionToken) {
        var refreshedUserSession = this.authenticationService.refreshSession(sessionToken);

        var sessionCookie = this.buildSessionCookie(refreshedUserSession.userSession());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                .body(new TokenDTO(refreshedUserSession.token()));
    }

    @GetMapping(Endpoint.Authentication.ACTIVATE_USER)
    public ResponseEntity<Void> activateUser(@RequestParam String code) {
        this.authenticationService.activateUser(code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.RESEND_ACTIVATION_CODE)
    public ResponseEntity<Void> resendActivationCode(@RequestBody @Valid ResendCodeDTO resendCodeRequestDTO) {
        this.authenticationService.resendActivationCode(resendCodeRequestDTO.login());
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

    private ResponseCookie buildSessionCookie(UserSession sessionToken) {
        return ResponseCookie.from("session_token", sessionToken.getSessionToken().toString())
                .httpOnly(true)
                .maxAge(this.authenticationService.getSessionDuration())
                .path("/")
                .build();
    }

    private ResponseCookie buildInvalidateSessionCookie() {
        return ResponseCookie.from("session_token", "")
                .httpOnly(true)
                .maxAge(Duration.ZERO)
                .path("/")
                .build();
    }

}
