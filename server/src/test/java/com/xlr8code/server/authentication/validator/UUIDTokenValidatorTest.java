package com.xlr8code.server.authentication.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UUIDTokenValidatorTest {

    private static final String VALID_UUID_STRING = "00000000-0000-0000-0000-000000000000";

    private UUIDTokenValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new UUIDTokenValidator();
    }

    @Test
    void should_accept_valid_uuids_strings() {
        var actual = this.validator.isValid(VALID_UUID_STRING, null);

        assertTrue(actual);
    }

    @Test
    void should_reject_invalid_uuids_strings() {
        var actual = this.validator.isValid("invalid", null);

        assertFalse(actual);
    }

}