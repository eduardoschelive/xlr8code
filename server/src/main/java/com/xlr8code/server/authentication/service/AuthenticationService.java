package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.signin.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpRequestDTO;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
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

    private User authenticate(AbstractAuthenticationToken authenticationToken) {
        try {
            var authentication = this.authenticationManager.authenticate(authenticationToken);
            return (User) authentication.getPrincipal();
        } catch (DisabledException e) {
            throw new ApplicationException(AuthenticationExceptionType.ACCOUNT_NOT_ACTIVATED);
        } catch (BadCredentialsException e) {
            throw new ApplicationException(AuthenticationExceptionType.INCORRECT_USERNAME_OR_PASSWORD);
        }
    }

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

        return this.authenticate(usernamePasswordAuthenticationToken);
    }

}
