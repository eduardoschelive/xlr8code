package com.xlr8code.server.authentication.controller;

import com.xlr8code.server.authentication.dto.SignUpBodyDTO;
import com.xlr8code.server.authentication.utils.Endpoint;
import com.xlr8code.server.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping(Endpoint.SIGN_UP)
    public ResponseEntity signUp(@RequestBody @Valid SignUpBodyDTO signUpBodyDTO) {
        this.userService.create(signUpBodyDTO.username(), signUpBodyDTO.email(), signUpBodyDTO.password());

        return ResponseEntity.ok().build();
    }

    @PostMapping(Endpoint.SIGN_IN)
    public void signIn() {
        throw new UnsupportedOperationException();
    }

}
