package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    /**
     * @param expirationTime the amount of time to be added to the current date
     * @param chronoUnit     the unit of time to be added to the current date
     * @return the date corresponding to the current date plus the given amount of time
     */
    public static Instant calculateExpiresAt(long expirationTime, ChronoUnit chronoUnit) {
        return Instant.now().plus(expirationTime, chronoUnit);
    }

    /**
     * @param expiresAt the date to be checked
     * @return true if the given date is before the current date, false otherwise
     */
    public static boolean isExpired(Instant expiresAt) {
        return expiresAt.isBefore(Instant.now());
    }

}
