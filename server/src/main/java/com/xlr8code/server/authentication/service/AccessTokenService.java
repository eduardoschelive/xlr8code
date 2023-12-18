package com.xlr8code.server.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Getter
public class AccessTokenService {

    @Value("${jwt.access-token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration-time}")
    private long expirationTime;

    @Value("${jwt.access-token.unit}")
    private String unit;

    public String generate(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("language", user.getMetadata().getLanguagePreference().getCode())
                    .withClaim("theme", user.getMetadata().getThemePreference().getCode())
                    .withClaim("profilePictureUrl", user.getMetadata().getProfilePictureUrl())
                    .withClaim("roles", user.getNamedRoles().stream().toList())
                    .withExpiresAt(this.getExpiresAt())
                    .withIssuedAt(this.getIssuedAt())
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException e) {
            throw new ApplicationException(AuthenticationExceptionType.JWT_CREATION_ERROR);
        }
    }

    @Nullable
    public DecodedJWT validate(String token) {
        try {
            return JWT.require(this.getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.getSecretKey());
    }

    private Instant getExpiresAt() {
        return Instant.now().plus(this.getExpirationTime(), this.getChronoUnit());
    }

    private Instant getIssuedAt() {
        return Instant.now();
    }

    private ChronoUnit getChronoUnit() {
        var unitName = this.getUnit().toUpperCase();
        return ChronoUnit.valueOf(unitName);
    }

}
