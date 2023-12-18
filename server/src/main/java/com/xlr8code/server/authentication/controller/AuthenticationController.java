package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.service.AuthenticationService;
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

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpBodyDTO) {
        var user = this.authenticationService.signUp(signUpBodyDTO);

        var token = this.authenticationService.generateAccessToken(user);
        var refreshToken = this.authenticationService.generateRefreshToken(user);

        var response = new SignUpResponseDTO(token, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO) {
        var user = this.authenticationService.signIn(signInRequestDTO);

        var token = this.authenticationService.generateAccessToken(user);
        var refreshToken = this.authenticationService.generateRefreshToken(user);

        return ResponseEntity.ok(new SignInResponseDTO(token, refreshToken));
    }

    @PostMapping(Endpoint.Authentication.REFRESH_TOKEN)
    public ResponseEntity<SignInResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var token = refreshTokenRequestDTO.refreshToken();
        return ResponseEntity.ok(this.authenticationService.refreshToken(token));
    }

}
