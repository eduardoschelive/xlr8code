package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.activation.ResendCodeRequestDTO;
import com.xlr8code.server.authentication.dto.password.ForgotPasswordRequestDTO;
import com.xlr8code.server.authentication.dto.password.ResetPasswordRequestDTO;
import com.xlr8code.server.authentication.dto.refresh.RefreshTokenRequestDTO;
import com.xlr8code.server.authentication.dto.refresh.RefreshTokenResponseDTO;
import com.xlr8code.server.authentication.dto.signin.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.signin.SignInResponseDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpRequestDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpResponseDTO;
import com.xlr8code.server.authentication.service.AccessTokenService;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.service.UserSessionService;
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
    private final AccessTokenService accessTokenService;
    private final UserSessionService refreshTokenService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpBodyDTO) {
        var user = this.authenticationService.signUp(signUpBodyDTO);

        var token = this.accessTokenService.generate(user);
        var userSession = this.refreshTokenService.create(user);

        var response = new SignUpResponseDTO(token, userSession.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO) {
        var user = this.authenticationService.signIn(signInRequestDTO);

        var token = this.accessTokenService.generate(user);
        var userSession = this.refreshTokenService.create(user);

        var response = new SignInResponseDTO(token, userSession.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var oldRefreshToken = refreshTokenRequestDTO.refreshTokenAsUUID();

        var userSession = this.refreshTokenService.validateSessionToken(oldRefreshToken);

        var newRefreshToken = this.refreshTokenService.refresh(userSession);
        var newAccessToken = this.accessTokenService.generate(userSession.getUser());

        var response = new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_OUT)
    public ResponseEntity<Void> revokeToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var token = refreshTokenRequestDTO.refreshTokenAsUUID();
        this.refreshTokenService.delete(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(Endpoint.Authentication.ACTIVATE_USER)
    public ResponseEntity<Void> activateUser(@RequestParam String code) {
        this.authenticationService.activateUser(code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.RESEND_ACTIVATION_CODE)
    public ResponseEntity<Void> resendActivationCode(@RequestBody @Valid ResendCodeRequestDTO resendCodeRequestDTO) {
        this.authenticationService.resendActivationCode(resendCodeRequestDTO.login());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.FORGOT_PASSWORD)
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        this.authenticationService.forgotPassword(forgotPasswordRequestDTO.login());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Endpoint.Authentication.RESET_PASSWORD)
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO) {
        var code = resetPasswordRequestDTO.code();
        var newPassword = resetPasswordRequestDTO.newPassword();
        this.authenticationService.resetPassword(code, newPassword);
        return ResponseEntity.noContent().build();
    }

}
