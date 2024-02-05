package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HashUtilsTest {

    private static final String MESSAGE = "test-message";

    @Test
    void it_should_not_be_equals_original_string() throws NoSuchAlgorithmException, InvalidKeyException {
        var hash = HashUtils.hash(MESSAGE, "test-key", HashUtils.Algorithm.HMAC_SHA512);

        assertNotEquals(MESSAGE, hash);
    }

    @Test
    void it_should_hash_to_same_string_if_called_twice() throws NoSuchAlgorithmException, InvalidKeyException {
        var hash1 = HashUtils.hash(MESSAGE, "test-key", HashUtils.Algorithm.HMAC_SHA512);
        var hash2 = HashUtils.hash(MESSAGE, "test-key", HashUtils.Algorithm.HMAC_SHA512);

        assertEquals(hash1, hash2);
    }

}