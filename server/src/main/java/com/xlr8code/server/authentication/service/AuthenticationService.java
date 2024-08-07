package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.dto.*;
import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.IncorrectUsernameOrPasswordException;
import com.xlr8code.server.common.service.EmailService;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.mail.AccountActivationMail;
import com.xlr8code.server.user.mail.PasswordResetMail;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserActivationCodeService userActivationCodeService;
    private final UserPasswordResetCodeService userPasswordResetCodeService;
    private final EmailService emailService;
    private final UserSessionService userSessionService;

    @Value("${user.activation-code.url}")
    private String activationCodeUrl;

    @Value("${user.password-reset-code.url}")
    private String passwordResetCodeUrl;

    /**
     * @param authenticationToken the authentication token to be used for authentication
     * @return the authenticated {@link User}
     * @throws AccountNotActivatedException         if the user is not activated
     * @throws IncorrectUsernameOrPasswordException if the username or currentPassword is incorrect
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
     * @return a {@link User} containing the user's data
     */
    @Transactional
    public User signUp(SignUpDTO signUpDTO) {
        var user = this.userService.create(signUpDTO.toCreateUserDTO());

        this.sendActivationMail(user);

        return user;
    }

    /**
     * @param signInDTO the sign-in request body
     * @return the session token generated
     */
    @Transactional
    public String signIn(SignInDTO signInDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signInDTO.login(), signInDTO.password());

        var user = this.authenticate(usernamePasswordAuthenticationToken);

        return this.userSessionService.generate(user);
    }

    /**
     * <p>
     * The user session is ended by removing the refresh session token from the database.
     * </p>
     *
     * @param sessionToken the refresh session token to be removed
     * @see UserSessionService#end(String)
     */
    @Transactional
    public void signOut(String sessionToken) {
        this.userSessionService.end(sessionToken);
    }

    /**
     * @param sessionToken the refresh session request body
     * @return the refreshed session token
     * @see UserSessionService#validateSessionToken(String)
     */
    @Transactional
    public String refreshSession(String sessionToken) {
        var userSession = this.userSessionService.validateSessionToken(sessionToken);
        return this.userSessionService.refresh(userSession);
    }

    /**
     * @param code the activation code
     * @return the activated user's session token
     * @throws AccountAlreadyActivatedException if the user is already activated
     */
    @Transactional
    public String activateUser(String code) {
        var activationCode = this.userActivationCodeService.validate(code);
        var user = activationCode.getUser();

        this.userService.activate(user);
        this.userActivationCodeService.removeAllFromUser(user);

        return this.userSessionService.generate(user);
    }

    /**
     * <p>
     * The activation code is sent to the user's email address.
     * </p>
     *
     * @param resendCodeDTO a {@link ResendCodeDTO} containing the user's login
     * @throws AccountAlreadyActivatedException if the user is already activated
     * @throws UserNotFoundException            if the user is not found
     * @see UserActivationCodeService#generate(User)
     */
    @Transactional
    public void resendActivationCode(ResendCodeDTO resendCodeDTO) {
        var login = resendCodeDTO.login();
        var user = this.userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

        this.sendActivationMail(user);
    }

    /**
     * <p>
     * The currentPassword reset code is sent to the user's email address.
     * </p>
     *
     * @param forgotPasswordDTO the forgot currentPassword request body
     * @throws UserNotFoundException if the user is not found
     * @see UserPasswordResetCodeService#generate(User)
     */
    @Transactional
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        var login = forgotPasswordDTO.login();
        var user = this.userService.findByLogin(login).orElseThrow(UserNotFoundException::new);

        var passwordResetCode = this.userPasswordResetCodeService.generate(user);

        var userLocale = user.getPreferences().getLanguage().getCode();

        var passwordResetEmail = PasswordResetMail.builder()
                .to(new String[]{user.getEmail()})
                .username(user.getUsername())
                .passwordResetCode(passwordResetCode.getCode())
                .passwordResetUrl(this.passwordResetCodeUrl + passwordResetCode.getCode())
                .locale(Locale.of(userLocale))
                .build();

        this.emailService.sendMail(passwordResetEmail);
    }

    /**
     * @param resetPasswordDTO the reset currentPassword request body
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

    /**
     * @return the duration of the session in seconds
     */
    public long getSessionDuration() {
        return this.userSessionService.getSessionDuration();
    }

    /**
     * @param user the user to send the activation email to
     */
    private void sendActivationMail(User user) {
        var activationCode = this.userActivationCodeService.generate(user);

        var userLocale = user.getPreferences().getLanguage().getCode();

        var activationEmail = AccountActivationMail.builder()
                .to(new String[]{user.getEmail()})
                .username(user.getUsername())
                .activationCode(activationCode.getCode())
                .activationUrl(this.activationCodeUrl + activationCode.getCode())
                .locale(Locale.of(userLocale))
                .build();

        this.emailService.sendMail(activationEmail);
    }


}

