package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.refresh.RefreshTokenRequestDTO;
import com.xlr8code.server.authentication.dto.refresh.RefreshTokenResponseDTO;
import com.xlr8code.server.authentication.dto.revoke.RevokeTokenRequestDTO;
import com.xlr8code.server.authentication.dto.signin.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.signin.SignInResponseDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpRequestDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpResponseDTO;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.service.token.AccessTokenService;
import com.xlr8code.server.authentication.service.token.RefreshTokenService;
import com.xlr8code.server.authentication.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.Authentication.BASE_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpBodyDTO) {
        var user = this.authenticationService.signUp(signUpBodyDTO);

        var token = this.accessTokenService.generate(user);
        var refreshToken = this.refreshTokenService.generate(user);

        var response = new SignUpResponseDTO(token, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO) {
        var user = this.authenticationService.signIn(signInRequestDTO);

        var token = this.accessTokenService.generate(user);
        var refreshToken = this.refreshTokenService.generate(user);

        var response = new SignInResponseDTO(token, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var oldRefreshToken = refreshTokenRequestDTO.refreshToken();

        var refreshToken = this.refreshTokenService.validate(oldRefreshToken);

        var newRefreshToken = this.refreshTokenService.refresh(refreshToken.getToken());
        var newAccessToken = this.accessTokenService.generate(refreshToken.getUser());

        var response = new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.REVOKE_TOKEN)
    public ResponseEntity<Void> revokeToken(@RequestBody @Valid RevokeTokenRequestDTO refreshTokenRequestDTO) {
        var token = refreshTokenRequestDTO.token();
        this.refreshTokenService.revoke(token);
        return ResponseEntity.ok().build();
    }

}
