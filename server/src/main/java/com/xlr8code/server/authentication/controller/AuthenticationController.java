package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.SignUpBodyDTO;
import com.xlr8code.server.authentication.dto.SignUpResponseDTO;
import com.xlr8code.server.authentication.service.VerificationTokenService;
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
    private final VerificationTokenService verificationTokenService;

    @PostMapping(Endpoint.SIGN_UP)
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpBodyDTO signUpBodyDTO, HttpServletRequest request) {
        var user = this.userService.create(signUpBodyDTO.username(), signUpBodyDTO.email(), signUpBodyDTO.password(), request);
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

    @GetMapping(Endpoint.VERIFY_TOKEN)
    public ResponseEntity<Void> verifyToken(@RequestParam("token") String token) {
        this.verificationTokenService.verify(token);
        return ResponseEntity.ok().build();
    }

}
