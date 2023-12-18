package com.xlr8code.server.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.exception.AuthenticationException;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    public String generateAccessToken(User user) throws ApplicationException {
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("language", user.getMetadata().getLanguagePreference().getCode())
                    .withClaim("theme", user.getMetadata().getThemePreference().getCode())
                    .withClaim("profilePictureUrl", user.getMetadata().getProfilePictureUrl())
                    .withClaim("roles", user.getNamedRoles().stream().toList())
                    .withExpiresAt(getExpirationDate())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException e) {
            throw new ApplicationException(AuthenticationException.JWT_CREATION_ERROR);
        }
    }

    @Nullable
    public DecodedJWT validateAccessToken(String token) throws ApplicationException {
        try {
            return JWT.require(this.getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(this.expirationTime).toInstant(ZoneOffset.of("+00:00"));
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.secretKey);
    }

}
