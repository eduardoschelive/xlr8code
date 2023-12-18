package com.xlr8code.server.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.exception.ApplicationJWTCreationException;
import com.xlr8code.server.common.utils.TimeUtils;
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
    private ChronoUnit chronoUnit;

    public String generate(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("language", user.getMetadata().getLanguagePreference().getCode())
                    .withClaim("theme", user.getMetadata().getThemePreference().getCode())
                    .withClaim("profilePictureUrl", user.getMetadata().getProfilePictureUrl())
                    .withClaim("roles", user.getNamedRoles().stream().toList())
                    .withExpiresAt(TimeUtils.calculateExpiresAt(this.getExpirationTime(), this.getChronoUnit()))
                    .withIssuedAt(this.getIssuedAt())
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException e) {
            throw new ApplicationJWTCreationException();
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

    private Instant getIssuedAt() {
        return Instant.now();
    }

}