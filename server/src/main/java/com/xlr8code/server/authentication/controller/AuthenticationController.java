package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.SignInResponseDTO;
import com.xlr8code.server.authentication.dto.SignUpRequestDTO;
import com.xlr8code.server.authentication.dto.SignUpResponseDTO;
import com.xlr8code.server.authentication.service.AuthenticationService;
import com.xlr8code.server.authentication.service.TokenService;
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
    private final TokenService tokenService;

    @PostMapping(Endpoint.Authentication.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpBodyDTO) {
        var user = this.authenticationService.signUp(signUpBodyDTO);
        var response = new SignUpResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getNamedRoles(),
                user.getMetadata().getLanguagePreference().getCode(),
                user.getMetadata().getThemePreference().getCode(),
                user.getMetadata().getProfilePictureUrl()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.Authentication.SIGN_IN)
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO) {

        var user = this.authenticationService.signIn(signInRequestDTO);
        var token = this.tokenService.generateAccessToken(user);

        var response = new SignInResponseDTO(token);

        return ResponseEntity.ok(response);
    }

}
