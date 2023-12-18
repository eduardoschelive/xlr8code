package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.SignUpRequestDTO;
import com.xlr8code.server.authentication.dto.SignUpResponseDTO;
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
    public void signIn() {
        throw new UnsupportedOperationException();
    }

}
