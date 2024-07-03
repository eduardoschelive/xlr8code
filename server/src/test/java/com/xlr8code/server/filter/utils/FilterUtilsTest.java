package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.entity.FilterTestEntity;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterUtilsTest {

    static Stream<String> params() {
        return Stream.of(
                "testEntity.stringRelationField_eq",
                "testEntity.booleanRelationField_neq"
        );
    }

    @ParameterizedTest
    @MethodSource("params")
    void it_should_extract_field_path(String key) {
        var expected = key.split("_")[0];

        var result = FilterUtils.extractFieldPath(key);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("params")
    void it_should_extract_field_operation(String key) {
        var expected = key.split("_")[1];

        var result = FilterUtils.extractOperation(key);

        assertEquals(expected, result);
    }

    @Test
    void it_should_extract_null_field_path() {
        var key = "eq";

        var result = FilterUtils.extractFieldPath(key);

        assertNull(result);
    }

    @Test
    void it_should_extract_null_field_operation() {
        var key = "testEntity";

        var result = FilterUtils.extractOperation(key);

        assertNull(result);
    }

    @Test
    void it_should_extract_filterable_fields() {
        var result = FilterUtils.extractFilterableFields(FilterTestEntity.class);

        assertEquals(6, result.size());
        assertTrue(result.containsKey("stringField"));
        assertTrue(result.containsKey("booleanField"));
        assertTrue(result.containsKey("testRelationEntity.stringRelationField"));
        assertTrue(result.containsKey("testRelationEntity.booleanRelationField"));
    }

}