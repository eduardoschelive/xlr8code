package com.xlr8code.server.filter.enums;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterOperationTest {

    @Test
    void it_should_return_filter_operation_from_suffix() {
        var expected = FilterOperation.EQUALITY;
        var actual = FilterOperation.fromSuffix("eq");

        assertEquals(expected, actual);
    }

    @Test
    void it_should_return_true_for_supported_suffix() {
        var actual = FilterOperation.isSupported("eq");

        assertTrue(actual);
    }

    @Test
    void it_should_return_false_for_unsupported_suffix() {
        var actual = FilterOperation.isSupported("unsupported");

        assertFalse(actual);
    }

}