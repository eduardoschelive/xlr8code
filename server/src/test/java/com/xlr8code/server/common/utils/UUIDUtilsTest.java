package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UUIDUtilsTest {

    @Test
    void it_should_return_empty_when_invalid_uuid() {
        var invalidUUID = "invalid";

        var uuid = UUIDUtils.convertFromString(invalidUUID);

        assertTrue(uuid.isEmpty());
    }

    @Test
    void it_should_return_uuid_when_valid_uuid() {
        var validUUID = "123e4567-e89b-12d3-a456-426614174000";

        var uuid = UUIDUtils.convertFromString(validUUID);

        assertTrue(uuid.isPresent());
    }

}