package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.common.service.EmailService;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.Role;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserActivationCodeService userActivationCodeService;
    private final UserPasswordResetCodeService userPasswordResetCodeService;
    private final EmailService emailService;
    private final AccessTokenService accessTokenService;
    private final UserSessionService userSessionService;

    private User authenticate(AbstractAuthenticationToken authenticationToken) {
        try {
            var authentication = this.authenticationManager.authenticate(authenticationToken);
            return (User) authentication.getPrincipal();
        } catch (DisabledException e) {
            throw new AccountNotActivatedException();
        } catch (BadCredentialsException e) {
            throw new IncorrectUsernameOrPasswordException();
        }
    }

    @Transactional
    public void signUp(SignUpDTO signUpDTO) {
        var user = this.buildUserWithMetadata(signUpDTO);

        this.userService.create(user);
    }

    @Transactional
    public TokenPairDTO signIn(SignInDTO signInRequestDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequestDTO.login(),
                signInRequestDTO.password()
        );

        var user = this.authenticate(usernamePasswordAuthenticationToken);

        var token = this.accessTokenService.generate(user);
        var userSession = this.userSessionService.create(user);

        return new TokenPairDTO(token, userSession.getSessionToken());
    }

    @Transactional
    public void signOut(SignOutDTO signOutDTO) {
        var sessionToken = UUID.fromString(signOutDTO.sessionToken());
        this.userSessionService.end(sessionToken);
    }

    @Transactional
    public TokenPairDTO refreshSession(RefreshSessionDTO refreshSessionDTO) {
        var oldRefreshSessionToken = UUID.fromString(refreshSessionDTO.refreshSessionToken());

        var userSession = this.userSessionService.validateSessionToken(oldRefreshSessionToken);

        var newRefreshSessionToken = this.userSessionService.refresh(userSession);
        var newAccessToken = this.accessTokenService.generate(userSession.getUser());

        return new TokenPairDTO(newAccessToken, newRefreshSessionToken);
    }

    @Transactional
    public void activateUser(String code) {
        var activationCode = this.userActivationCodeService.validate(code);

        var user = activationCode.getUser();
        this.userService.activate(user);

        this.userActivationCodeService.removeAllFromUser(user);
    }

    @Transactional
    public void resendActivationCode(String login) {
        var user = this.userService.findByLogin(login)
                .orElseThrow(UserNotFoundException::new);

        if (user.isActive()) {
            throw new AccountAlreadyActivatedException();
        }

        var activationCode = this.userActivationCodeService.generate(user);

        this.emailService.sendActivationEmail(user.getEmail(), activationCode.getCode());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        var login = forgotPasswordDTO.login();
        var user = this.userService.findByLogin(login)
                .orElseThrow(UserNotFoundException::new);

        var passwordResetCode = this.userPasswordResetCodeService.generate(user);

        this.emailService.sendPasswordResetEmail(user.getEmail(), passwordResetCode.getCode());
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        var code = resetPasswordDTO.code();
        var passwordResetCode = this.userPasswordResetCodeService.validate(code);

        var newPassword = resetPasswordDTO.newPassword();
        var confirmPassword = resetPasswordDTO.newPasswordConfirmation();

        var user = passwordResetCode.getUser();

        this.userService.changePassword(user, newPassword, confirmPassword);
        this.userPasswordResetCodeService.removeAllFromUser(user);
    }

    private User buildUserWithMetadata(SignUpDTO signUpDTO) {
        var roles = Set.of(UserRole.DEFAULT.toRole());

        var user = this.buildUser(signUpDTO, roles);
        var userMetadata = this.buildUserMetadata(user, signUpDTO);

        user.setMetadata(userMetadata);

        return user;
    }

    private UserMetadata buildUserMetadata(User user, SignUpDTO signUpBodyDTO) {
        var language = Language.fromCode(signUpBodyDTO.languagePreference());
        var theme = Theme.fromCode(signUpBodyDTO.themePreference());

        return UserMetadata.builder()
                .languagePreference(language)
                .user(user)
                .themePreference(theme)
                .profilePictureUrl(signUpBodyDTO.profilePictureUrl())
                .build();
    }

    private User buildUser(SignUpDTO signUpBodyDTO, Set<Role> roles) {
        return User.builder()
                .username(signUpBodyDTO.username())
                .email(signUpBodyDTO.email())
                .password(signUpBodyDTO.password())
                .roles(roles)
                .build();
    }

}

