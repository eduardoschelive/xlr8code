package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.SignUpBodyDTO;
import com.xlr8code.server.authentication.dto.SignUpResponseDTO;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.authentication.utils.Endpoint;
import com.xlr8code.server.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final LocaleService  localeService;

    @PostMapping(Endpoint.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpBodyDTO signUpBodyDTO) {
        var user = this.userService.create(signUpBodyDTO.username(), signUpBodyDTO.email(), signUpBodyDTO.password());
        var response = new SignUpResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getRoles()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoint.SIGN_IN)
    public void signIn() {
        throw new UnsupportedOperationException();
    }

}
