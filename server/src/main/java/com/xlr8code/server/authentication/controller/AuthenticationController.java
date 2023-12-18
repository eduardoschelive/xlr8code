package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Authentication.BASE_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDTO signUpBodyDTO) {
        this.authenticationService.signUp(signUpBodyDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<TokenPairDTO> signIn(@RequestBody @Valid SignInDTO signInRequestDTO) {
        var response = this.authenticationService.signIn(signInRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> revokeToken(@RequestBody @Valid SignOutDTO signOutDTO) {
        this.authenticationService.signOut(signOutDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.REFRESH_SESSION)
    public ResponseEntity<TokenPairDTO> refreshSession(@RequestBody @Valid RefreshSessionDTO refreshSessionDTO) {
        var response = this.authenticationService.refreshSession(refreshSessionDTO);
        return ResponseEntity.ok(response);
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

}
