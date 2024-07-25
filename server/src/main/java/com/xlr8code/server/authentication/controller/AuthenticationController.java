package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.exception.*;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.utils.SessionCookieUtils;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.openapi.annotation.ErrorResponses;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Authentication.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Sign up",
            description = "Use this endpoint to sign up a new user.",
            responses = @ApiResponse(responseCode = "201")
    )
    @ErrorResponses(value = {
            EmailAlreadyInUseException.class,
            UsernameAlreadyTakenException.class
    })
    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDTO signUpBodyDTO) {
        var newUser = this.authenticationService.signUp(signUpBodyDTO);

        var createdUserURI = URI.create(Endpoint.User.BASE_PATH + "/" + newUser.getId());

        return ResponseEntity.created(createdUserURI).build();
    }

    @Operation(
            summary = "Sign in",
            description = "Use this endpoint to sign in a user.",
            responses = @ApiResponse(responseCode = "204", headers = @Header(name = HttpHeaders.SET_COOKIE, description = "`" + SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME + "` cookie containing the session token"))
    )
    @ErrorResponses(value = {
            AccountNotActivatedException.class,
            IncorrectUsernameOrPasswordException.class
    })
    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<Void> signIn(@RequestBody @Valid SignInDTO signInRequestDTO) {
        var sessionToken = this.authenticationService.signIn(signInRequestDTO);
        var duration = signInRequestDTO.rememberMe() ? this.authenticationService.getSessionDuration() : -1;
        var cookie = SessionCookieUtils.createSessionToken(sessionToken, duration);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @Operation(
            summary = "Sign out",
            description = "Use this endpoint to sign out a user.",
            responses = @ApiResponse(responseCode = "204", headers = @Header(name = HttpHeaders.SET_COOKIE, description = "`" + SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME + "` cookie with an empty value and an expiration date in the past"))
    )
    @ErrorResponses(value = InvalidRefreshSessionTokenException.class)
    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> signOut(
            @Schema(description = "The current value of session token") @CookieValue(SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME) String sessionToken
    ) {
        this.authenticationService.signOut(sessionToken);

        var invalidateSessionCookie = SessionCookieUtils.createSessionToken("", 0);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, invalidateSessionCookie).build();
    }


    @Operation(
            summary = "Refresh session",
            description = "Use this endpoint to refresh the session of a user.",
            responses = @ApiResponse(responseCode = "204", headers = @Header(name = HttpHeaders.SET_COOKIE, description = "`" + SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME + "` cookie containing the refreshed session token"))
    )
    @ErrorResponses(value = InvalidRefreshSessionTokenException.class)
    @PostMapping(Endpoint.Authentication.REFRESH_SESSION)
    public ResponseEntity<Void> refreshSession(
            @Schema(description = "The current value of session token") @CookieValue(SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME) String sessionToken
    ) {
        var refreshedSessionToken = this.authenticationService.refreshSession(sessionToken);
        var cookie = SessionCookieUtils.createSessionToken(refreshedSessionToken, this.authenticationService.getSessionDuration());

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @Operation(
            summary = "Activate user",
            description = "Use this endpoint to activate a user.",
            responses = @ApiResponse(responseCode = "204", headers = @Header(name = HttpHeaders.SET_COOKIE, description = "`" + SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME + "` cookie containing the session token that will expire when the user closes the browser"))
    )
    @ErrorResponses(value = {
            InvalidRefreshSessionTokenException.class,
            ExpiredActivationCodeException.class,
            AccountAlreadyActivatedException.class
    })
    @PostMapping(Endpoint.Authentication.ACTIVATE_USER)
    public ResponseEntity<Void> activateUser(@RequestBody @Valid ActivateUserDTO activateUserDTO) {
        var code = activateUserDTO.code();
        var sessionToken = this.authenticationService.activateUser(code);

        // expire when user closes the browser
        var cookie = SessionCookieUtils.createSessionToken(sessionToken, -1);

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @Operation(
            summary = "Resend activation code",
            description = "Use this endpoint to resend the activation code to the user.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = {
            UserNotFoundException.class,
            AccountAlreadyActivatedException.class
    })
    @PostMapping(Endpoint.Authentication.RESEND_ACTIVATION_CODE)
    public ResponseEntity<Void> resendActivationCode(@RequestBody @Valid ResendCodeDTO resendCodeRequestDTO) {
        this.authenticationService.resendActivationCode(resendCodeRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Forgot password",
            description = "Use this endpoint to request a password reset.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = UserNotFoundException.class)
    @PostMapping(Endpoint.Authentication.FORGOT_PASSWORD)
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDTO forgotPasswordRequestDTO) {
        this.authenticationService.forgotPassword(forgotPasswordRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reset password",
            description = "Use this endpoint to reset the password of a user with a valid reset code.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = {
            ExpiredPasswordResetCodeException.class,
            InvalidPasswordResetCodeException.class,
            PasswordMatchException.class
    })
    @PostMapping(Endpoint.Authentication.RESET_PASSWORD)
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordRequestDTO) {
        this.authenticationService.resetPassword(resetPasswordRequestDTO);

        return ResponseEntity.noContent().build();
    }

}
