package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.InvalidRefreshSessionTokenException;
import com.xlr8code.server.authentication.exception.SessionExpiredException;
import com.xlr8code.server.authentication.exception.UserSessionCreationException;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.common.utils.HashUtils;
import com.xlr8code.server.common.utils.RandomUtils;
import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    @Value("${user.session.key}")
    private String key;

    /**
     * <p>
     * Creates a new session for the given user. The expiration date is configured in the application properties.
     * </p>
     *
     * @param user         the user to create the session for
     * @param sessionToken the session token to be used
     * @return the created {@link UserSession}
     */
    @Transactional
    public UserSession create(User user, String sessionToken) {
        var sessionTokenHash = this.hashSessionToken(sessionToken);

        var userSession = UserSession.builder()
                .user(user)
                .sessionToken(sessionTokenHash)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userSessionRepository.save(userSession);
    }

    @Transactional
    public String generate(User user) {
        var sessionToken = this.generateSessionToken();

        this.create(user, sessionToken);

        return sessionToken;
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
        var sessionTokenHash = this.hashSessionToken(token);

        var userSession = this.userSessionRepository.findBySessionToken(sessionTokenHash)
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
     * @return the new session token
     */
    @Transactional
    public String refresh(UserSession userSession) {
        var newSessionToken = this.generateSessionToken();
        var sessionTokenHash = this.hashSessionToken(newSessionToken);
        var newExpiresAt = DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit);

        userSession.refresh(sessionTokenHash, newExpiresAt);
        this.userSessionRepository.save(userSession);

        return newSessionToken;
    }

    /**
     * @param token the token to end
     * @throws InvalidRefreshSessionTokenException if the token is not found in the database
     */
    @Transactional
    public void end(String token) {
        var sessionTokenHash = this.hashSessionToken(token);

        var refreshToken = this.userSessionRepository.findBySessionToken(sessionTokenHash)
                .orElseThrow(InvalidRefreshSessionTokenException::new);

        this.userSessionRepository.delete(refreshToken);
    }

    /**
     * <p>
     * Removes all sessions from the given user. This method is used when the user changes his currentPassword.
     * All sessions are removed so that the user has to log in again with the new currentPassword.
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

    private String hashSessionToken(String sessionToken) {
        try {
            return HashUtils.hash(sessionToken, this.key, HashUtils.Algorithm.HMAC_SHA512);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UserSessionCreationException();
        }
    }

}
