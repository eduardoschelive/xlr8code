package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {

    public static Date calculateExpiresAt(long expirationTime, ChronoUnit chronoUnit) {
        return Date.from(Instant.now().plus(expirationTime, chronoUnit));
    }

}
