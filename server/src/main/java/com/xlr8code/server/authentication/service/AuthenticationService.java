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

    /**
     * @param authenticationToken the authentication token to be used for authentication
     * @return the authenticated user
     * @throws AccountNotActivatedException         if the user is not activated
     * @throws IncorrectUsernameOrPasswordException if the username or password is incorrect
     */
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

    /**
     * @param signUpDTO the sign-up request body
     */
    @Transactional
    public void signUp(SignUpDTO signUpDTO) {
        var user = this.buildUserWithMetadata(signUpDTO);

        this.userService.create(user);
    }

    /**
     * @param signInRequestDTO the sign-in request body
     * @return a {@link TokenPairDTO} containing the access token and the refresh session token
     * @see AccessTokenService#generate(User)
     */
    @Transactional
    public TokenPairDTO signIn(SignInDTO signInRequestDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signInRequestDTO.login(), signInRequestDTO.password());

        var user = this.authenticate(usernamePasswordAuthenticationToken);

        var token = this.accessTokenService.generate(user);
        var userSession = this.userSessionService.create(user);

        return new TokenPairDTO(token, userSession.getSessionToken());
    }

    /**
     * <p>
     * The user session is ended by removing the refresh session token from the database.
     * </p>
     *
     * @param signOutDTO, a {@link SignOutDTO} containing the refresh session token to be removed
     * @see UserSessionService#end(UUID)
     */
    @Transactional
    public void signOut(SignOutDTO signOutDTO) {
        var sessionToken = UUID.fromString(signOutDTO.sessionToken());
        this.userSessionService.end(sessionToken);
    }

    /**
     * @param refreshSessionDTO the refresh session request body
     * @return a {@link TokenPairDTO} containing the new access token and the new refresh session token
     * @see UserSessionService#validateSessionToken(UUID)
     */
    @Transactional
    public TokenPairDTO refreshSession(RefreshSessionDTO refreshSessionDTO) {
        var oldRefreshSessionToken = UUID.fromString(refreshSessionDTO.refreshSessionToken());

        var userSession = this.userSessionService.validateSessionToken(oldRefreshSessionToken);

        var newRefreshSessionToken = this.userSessionService.refresh(userSession);
        var newAccessToken = this.accessTokenService.generate(userSession.getUser());

        return new TokenPairDTO(newAccessToken, newRefreshSessionToken);
    }

    /**
     * @param code the activation code
     * @throws AccountAlreadyActivatedException if the user is already activated
     */
    @Transactional
    public void activateUser(String code) {
        var activationCode = this.userActivationCodeService.validate(code);

        var user = activationCode.getUser();
        this.userService.activate(user);

        this.userActivationCodeService.removeAllFromUser(user);
    }

    /**
     * <p>
     * The activation code is sent to the user's email address.
     * </p>
     *
     * @param login the login of the user to whom the activation code will be sent
     * @throws AccountAlreadyActivatedException if the user is already activated
     * @throws UserNotFoundException            if the user is not found
     * @see UserActivationCodeService#generate(User)
     */
    @Transactional
    public void resendActivationCode(String login) {
        var user = this.userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

        if (user.isActive()) {
            throw new AccountAlreadyActivatedException();
        }

        var activationCode = this.userActivationCodeService.generate(user);

        this.emailService.sendActivationEmail(user.getEmail(), activationCode.getCode());
    }

    /**
     * <p>
     *     The password reset code is sent to the user's email address.
     * </p>
     * @param forgotPasswordDTO the forgot password request body
     * @throws UserNotFoundException if the user is not found
     * @see UserPasswordResetCodeService#generate(User)
     */
    @Transactional
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        var login = forgotPasswordDTO.login();
        var user = this.userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

        var passwordResetCode = this.userPasswordResetCodeService.generate(user);

        this.emailService.sendPasswordResetEmail(user.getEmail(), passwordResetCode.getCode());
    }

    /**
     * @param resetPasswordDTO the reset password request body
     * @see UserPasswordResetCodeService#validate(String)
     * @see UserService#changePassword(User, String, String)
     * @see UserPasswordResetCodeService#removeAllFromUser(User)
     */
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

        return UserMetadata.builder().languagePreference(language).user(user).themePreference(theme).profilePictureUrl(signUpBodyDTO.profilePictureUrl()).build();
    }

    private User buildUser(SignUpDTO signUpBodyDTO, Set<Role> roles) {
        return User.builder().username(signUpBodyDTO.username()).email(signUpBodyDTO.email()).password(signUpBodyDTO.password()).roles(roles).build();
    }

}

