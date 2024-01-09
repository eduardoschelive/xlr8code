package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RandomUtilsTest {

    @Test
    void it_should_generate_correct_length() {
        var length = 6;

        var randomCode = RandomUtils.generate(length);

        assertEquals(length, randomCode.length());
    }

    @Test
    void it_should_generate_only_allowed_characters() {
        var allowedCharacters = "0123456789!)%*";
        var matchRegex = "[" + allowedCharacters + "]+";

        var randomCode = RandomUtils.generate(6, allowedCharacters);

        assertTrue(randomCode.matches(matchRegex));
    }

    @Test
    void it_should_generate_alphanumeric_code() {
        var length = 6;
        var matchRegex = "[0-9A-Za-z]+";

        var randomCode = RandomUtils.generateAlphanumeric(length);

        assertTrue(randomCode.matches(matchRegex));
    }
}