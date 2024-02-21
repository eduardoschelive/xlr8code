package com.xlr8code.server.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.exception.ApplicationJWTCreationException;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AccessTokenService {

    @Value("${jwt.access-token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration-time}")
    private long expirationTime;

    @Value("${jwt.access-token.unit}")
    private ChronoUnit chronoUnit;

    /**
     * @param user the user to generate the token for
     * @return the generated token
     * @throws ApplicationJWTCreationException if the token could not be created
     */
    public String generate(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getId().toString())
                    .withClaim("roles", user.getNamedRoles().stream().toList())
                    .withExpiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                    .withIssuedAt(this.getIssuedAt())
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException e) {
            throw new ApplicationJWTCreationException();
        }
    }

    /**
     * @param token the token to decode
     * @return the validated token or null if the token is invalid or expired or null if the token is invalid or expired
     * @see DecodedJWT
     */
    public DecodedJWT decode(String token) {
        try {
            return JWT.require(this.getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.secretKey);
    }

    private Instant getIssuedAt() {
        return Instant.now();
    }

}
