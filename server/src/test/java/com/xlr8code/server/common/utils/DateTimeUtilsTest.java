package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DateTimeUtilsTest {

    @Test
    void it_should_calculate_expires_at() {
        long expirationTime = 1;

        var expiresAt = DateTimeUtils.calculateExpiresAt(expirationTime, ChronoUnit.SECONDS);

        assertNotNull(expiresAt);
    }

    @Test
    void it_should_check_if_expired() {
        var expiresAt = DateTimeUtils.calculateExpiresAt(-1, ChronoUnit.SECONDS);

        var isExpired = DateTimeUtils.isExpired(expiresAt);

        assertTrue(isExpired);
    }

    @Test
    void it_should_parse_instant() {
        var instant = "2021-08-01T00:00:00Z";

        var parsedInstant = DateTimeUtils.parseInstant(instant);

        assertNotNull(parsedInstant);
    }

    @Test
    void it_should_not_parse_instant() {
        var instant = "invalid";

        var parsedInstant = DateTimeUtils.parseInstant(instant);

        assertNull(parsedInstant);
    }


}