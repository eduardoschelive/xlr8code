package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DoubleUtilsTest {

    @Test
    void it_should_parse_double() {
        String value = "1.0";

        Double result = DoubleUtils.tryParse(value);

        assertEquals(1.0, result);
    }

    @Test
    void it_should_try_parse_with_default() {
        Double result = DoubleUtils.tryParse("invalid", 1.0);

        assertEquals(1.0, result);
    }
}