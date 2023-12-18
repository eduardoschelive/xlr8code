package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.authentication.exception.AccountNotActivatedException;
import com.xlr8code.server.authentication.exception.InvalidRefreshTokenException;
import com.xlr8code.server.authentication.exception.SessionExpiredException;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Value("${user.session.expiration-time}")
    private long expirationTime;

    @Value("${user.session.unit}")
    private ChronoUnit chronoUnit;

    @Transactional
    public UserSession create(User user) {
        var userSession = UserSession.builder()
                .user(user)
                .refreshToken(UUID.randomUUID())
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userSessionRepository.save(userSession);
    }

    @Transactional(readOnly = true)
    public UserSession validateSessionToken(UUID token) {
        var userSession = this.userSessionRepository.findByRefreshToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!userSession.getUser().isActive()) {
            throw new AccountNotActivatedException();
        }

        if (userSession.isExpired()) {
            throw new SessionExpiredException();
        }

        return userSession;
    }

    @Transactional
    public UUID refresh(UserSession userSession) {
        userSession.refresh();

        var refreshedUserSession = this.userSessionRepository.save(userSession);

        return refreshedUserSession.getRefreshToken();
    }

    @Transactional
    public void delete(UUID token) {
        var refreshToken = this.userSessionRepository.findByRefreshToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        this.userSessionRepository.delete(refreshToken);
    }

}
