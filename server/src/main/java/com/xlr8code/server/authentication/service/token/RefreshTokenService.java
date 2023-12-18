package com.xlr8code.server.authentication.service.token;

import com.xlr8code.server.authentication.entity.RefreshToken;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.authentication.repository.RefreshTokenRepository;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Getter
@RequiredArgsConstructor
public class RefreshTokenService implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-token.expiration-time}")
    private long expirationTime;

    @Value("${jwt.refresh-token.unit}")
    private String unit;

    @Override
    @Transactional
    public String generate(User user) {
        var refreshToken = this.getNewRefreshToken(user);
        var newRefreshToken = this.refreshTokenRepository.save(refreshToken);

        return newRefreshToken.getToken();
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        var refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        if (refreshToken.isExpired() || refreshToken.isRevoked()) {
            throw new ApplicationException(AuthenticationExceptionType.SESSION_EXPIRED);
        }

        return refreshToken;
    }

    @Transactional
    public String refresh(String token) {
        var refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        this.revoke(refreshToken);

        return this.generate(refreshToken.getUser());
    }

    @Transactional
    public void revoke(String token) {
        var refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_REFRESH_TOKEN));

        this.revoke(refreshToken);
    }

    private void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Date.from(Instant.now()));

        this.refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken getNewRefreshToken(User user) {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Date.from(this.getExpiresAt()))
                .build();
    }

}
