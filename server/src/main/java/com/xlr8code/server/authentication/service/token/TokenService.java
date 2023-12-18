package com.xlr8code.server.authentication.service.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.xlr8code.server.user.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public interface TokenService {

    long getExpirationTime();

    String getUnit();

    String getSecretKey();

    Object validate(String token);

    String generate(User user);

    default Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.getSecretKey());
    }

    default Instant getExpiresAt() {
        return Instant.now().plus(this.getExpirationTime(), this.getChronoUnit());
    }

    default Instant getIssuedAt() {
        return Instant.now();
    }

    default ChronoUnit getChronoUnit() {
        var unitName = this.getUnit().toUpperCase();
        return ChronoUnit.valueOf(unitName);
    }

}
