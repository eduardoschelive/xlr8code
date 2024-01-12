package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.InvalidRefreshSessionTokenException;
import com.xlr8code.server.authentication.exception.SessionExpiredException;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.common.utils.RandomUtils;
import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Value("${user.session.token-length}")
    private int tokenLength;

    @Value("${user.session.expiration-time}")
    private long expirationTime;

    @Value("${user.session.unit}")
    private ChronoUnit chronoUnit;

    /**
     * <p>
     * Creates a new session for the given user. The expiration date is configured in the application properties.
     * </p>
     *
     * @param user the user to create the session for
     * @return the created {@link UserSession}
     */
    @Transactional
    public UserSession create(User user) {
        var userSession = UserSession.builder()
                .user(user)
                .sessionToken(this.generateSessionToken())
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userSessionRepository.save(userSession);
    }

    /**
     * @param token the token to decode
     * @return the validated {@link UserSession}
     * @throws InvalidRefreshSessionTokenException if the token is invalid
     * @throws AccountNotActivatedException        if the user is not activated
     * @throws SessionExpiredException             if the session is expired
     */
    @Transactional(readOnly = true)
    public UserSession validateSessionToken(String token) {
        var userSession = this.userSessionRepository.findBySessionToken(token)
                .orElseThrow(InvalidRefreshSessionTokenException::new);

        if (!userSession.getUser().isActive())
            throw new AccountNotActivatedException();

        if (userSession.isExpired())
            throw new SessionExpiredException();

        return userSession;
    }

    /**
     * <p>
     * Refreshes the session by setting a new expiration date.
     * The expiration date is configured in the application properties.
     * </p>
     *
     * @param userSession the session to refresh
     * @return the refreshed {@link UserSession}
     */
    @Transactional
    public UserSession refresh(UserSession userSession) {
        var newSessionToken = this.generateSessionToken();
        var newExpiresAt = DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit);

        userSession.refresh(newSessionToken, newExpiresAt);

        return this.userSessionRepository.save(userSession);
    }

    /**
     * @param token the token to end
     * @throws InvalidRefreshSessionTokenException if the token is not found in the database
     */
    @Transactional
    public void end(String token) {
        var refreshToken = this.userSessionRepository.findBySessionToken(token)
                .orElseThrow(InvalidRefreshSessionTokenException::new);

        this.userSessionRepository.delete(refreshToken);
    }

    /**
     * <p>
     * Removes all sessions from the given user. This method is used when the user changes his password.
     * All sessions are removed so that the user has to log in again with the new password.
     * </p>
     *
     * @param user the user to remove the sessions from
     */
    @Transactional
    public void endAllFromUser(User user) {
        this.userSessionRepository.deleteAllByUser(user);
    }

    /**
     * @return the session duration in seconds
     */
    public long getSessionDuration() {
        return this.expirationTime * this.chronoUnit.getDuration().getSeconds();
    }

    private String generateSessionToken() {
        return RandomUtils.generateAlphanumeric(this.tokenLength);
    }

}
