package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Getter
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Value("${jwt.refresh-token.expiration-time}")
    private long expirationTime;

    @Value("${jwt.refresh-token.unit}")
    private String unit;

    @Transactional
    public UserSession generate(User user) {

        var userSession = new UserSession(user, this.getExpiresAt());

        return this.userSessionRepository.save(userSession);
    }

    @Transactional(readOnly = true)
    public UserSession validate(UUID token) {
        var userSession = this.userSessionRepository.findByRefreshToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        if (userSession.isExpired()) {
            throw new ApplicationException(AuthenticationExceptionType.SESSION_EXPIRED);
        }

        return userSession;
    }

    @Transactional
    public UUID refresh(UUID token) {
        var userSession = this.userSessionRepository.findByRefreshToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        if (userSession.isExpired()) {
            throw new ApplicationException(AuthenticationExceptionType.SESSION_EXPIRED);
        }

        userSession.refresh();

        var refreshedUserSession = this.userSessionRepository.save(userSession);

        return refreshedUserSession.getRefreshToken();
    }

    @Transactional
    public void revoke(UUID token) {
        var refreshToken = this.userSessionRepository.findByRefreshToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        this.userSessionRepository.delete(refreshToken);
    }

    private Date getExpiresAt() {
        return Date.from(Instant.now().plus(this.getExpirationTime(), this.getChronoUnit()));
    }

    private ChronoUnit getChronoUnit() {
        var unitName = this.getUnit().toUpperCase();
        return ChronoUnit.valueOf(unitName);
    }

}
