package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.exception.InvalidBooleanFilterValueException;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BooleanParsingStrategyTest {

    @Autowired
    private FilterTestRepository testRepository;

    @BeforeAll
    static void setup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.createTestEntity("stringField", true, null);
        filterTestUtils.createTestEntity("stringField", false, null);
        filterTestUtils.createTestEntity("stringField", null, null);
    }

    @AfterAll
    static void cleanup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.clearRepositories();
    }

    @Test
    void it_should_filter_with_boolean_field() {
        var params = Map.of(
                "booleanField_eq", "true"
        );

        var results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }

    @Test
    void it_should_filter_by_negated_boolean_field() {
        var params = Map.of(
                "booleanField_n-eq", "true"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }

    @Test
    void it_should_throw_exception_when_invalid_boolean_value() {
        var params = Map.of(
                "booleanField_eq", "invalid"
        );

        assertThrows(InvalidBooleanFilterValueException.class, () -> {
            testRepository.findAll(params, FilterTestEntity.class);
        });
    }

    @Test
    void it_should_filter_null_boolean_field() {
        var params = Map.of(
                "booleanField_null",  ""
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }


}