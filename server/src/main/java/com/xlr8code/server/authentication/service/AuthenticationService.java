package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.SignUpRequestDTO;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public User signUp(SignUpRequestDTO signUpBodyDTO) {
        var passwordHash = this.passwordEncoder.encode(signUpBodyDTO.password());
        var roles = Set.of(UserRole.DEFAULT.toRole());

        var language = Language.fromCode(signUpBodyDTO.languagePreference());
        var theme = Theme.fromCode(signUpBodyDTO.themePreference());

        var userMetadata = UserMetadata.builder()
                .languagePreference(language)
                .themePreference(theme)
                .profilePictureUrl(signUpBodyDTO.profilePictureUrl())
                .build();

        var user = User.builder()
                .username(signUpBodyDTO.username())
                .email(signUpBodyDTO.email())
                .passwordHash(passwordHash)
                .roles(roles)
                .build();

        return this.userService.createUserWithMetadata(user, userMetadata);
    }

    public String signIn(SignInRequestDTO signInRequestDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequestDTO.login(),
                signInRequestDTO.password()
        );

        var authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var user = (User) authentication.getPrincipal();

        return this.tokenService.generateAccessToken(user);
    }

    public String refreshToken(String token) {
        var validatedToken = this.tokenService.validateAccessToken(token);
        var username = validatedToken.getSubject();
        var user = this.userService.findByLogin(username).orElseThrow();

        return this.tokenService.generateAccessToken(user);
    }

}
