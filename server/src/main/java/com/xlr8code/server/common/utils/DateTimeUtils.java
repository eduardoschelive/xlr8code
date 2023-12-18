package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    public static Date calculateExpiresAt(long expirationTime, ChronoUnit chronoUnit) {
        return Date.from(Instant.now().plus(expirationTime, chronoUnit));
    }

    public static boolean isExpired(Date expiresAt) {
        return expiresAt.before(Date.from(Instant.now()));
    }

}
