package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.signin.SignInRequestDTO;
import com.xlr8code.server.authentication.dto.signup.SignUpRequestDTO;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.service.EmailService;
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
    private final UserActivationCodeService userActivationCodeService;
    private final UserPasswordResetCodeService userPasswordResetCodeService;
    private final EmailService emailService;

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

        var newUser = this.userService.createUserWithMetadata(user, userMetadata);

        var activationCode = this.userActivationCodeService.generate(newUser);

        this.emailService.sendActivationEmail(newUser.getEmail(), activationCode.getCode());

        return newUser;
    }

    public User signIn(SignInRequestDTO signInRequestDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequestDTO.login(),
                signInRequestDTO.password()
        );

        return this.authenticate(usernamePasswordAuthenticationToken);
    }

    @Transactional
    public void activateUser(String code) {
        var activationCode = this.userActivationCodeService.validate(code);

        // TODO: 2021-10-10 change the ApplicationError to be separated classes, so we can catch them separately to resend the activation code if needed (e.g. if the user's code is expired)

        var user = activationCode.getUser();
        this.userService.activate(user);

        this.userActivationCodeService.removeAllFromUser(user);
    }

    @Transactional
    public void resendActivationCode(String login) {
        var user = this.userService.findByLogin(login)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.USER_NOT_FOUND));

        if (user.isActive()) {
            throw new ApplicationException(AuthenticationExceptionType.ACCOUNT_ALREADY_ACTIVATED);
        }

        var activationCode = this.userActivationCodeService.generate(user);

        this.emailService.sendActivationEmail(user.getEmail(), activationCode.getCode());
    }

    @Transactional
    public void forgotPassword(String login) {
        var user = this.userService.findByLogin(login)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.USER_NOT_FOUND));

        var passwordResetCode = this.userPasswordResetCodeService.generate(user);

        this.emailService.sendPasswordResetEmail(user.getEmail(), passwordResetCode.getCode());
    }

    @Transactional
    public void resetPassword(String code, String newPassword) {
        var passwordResetCode = this.userPasswordResetCodeService.validate(code);

        // TODO: 2021-10-10 add a confirmation step to the password reset process, like newPasswordConfirmation

        var user = passwordResetCode.getUser();
        var passwordHash = this.passwordEncoder.encode(newPassword);

        this.userService.updatePassword(user, passwordHash);

        this.userPasswordResetCodeService.removeAllFromUser(user);
    }

}

