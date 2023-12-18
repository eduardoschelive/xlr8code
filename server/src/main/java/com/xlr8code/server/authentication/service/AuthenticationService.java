package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.SignInResponseDTO;
import com.xlr8code.server.authentication.dto.SignUpRequestDTO;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    public User signIn(SignInRequestDTO signInRequestDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequestDTO.login(),
                signInRequestDTO.password()
        );

        try {
            var authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return (User) authentication.getPrincipal();
        } catch (DisabledException e) {
            throw new ApplicationException(AuthenticationExceptionType.ACCOUNT_NOT_ACTIVATED);
        }
    }

    public String generateAccessToken(User user) {
        return this.tokenService.generateAccessToken(user);
    }

    public String generateRefreshToken(User user) {
        return this.tokenService.generateRefreshToken(user);
    }

    public SignInResponseDTO refreshToken(String token) {
        var validatedToken = this.tokenService.decodeToken(token);
        var username = validatedToken.getSubject();
        var user = this.userService.findByLogin(username).orElseThrow();

        return new SignInResponseDTO(
                this.tokenService.generateAccessToken(user),
                this.tokenService.generateRefreshToken(user)
        );
    }

}
