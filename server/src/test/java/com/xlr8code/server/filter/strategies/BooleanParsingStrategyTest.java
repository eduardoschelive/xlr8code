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

    private static final int TEST_SIZE = 5;

    @Autowired
    private FilterTestRepository testRepository;

    @BeforeAll
    static void setup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.createNEntities(TEST_SIZE, "stringField", true);
        filterTestUtils.createNEntities(TEST_SIZE, "stringField", false);
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
        assertEquals(TEST_SIZE, results.getTotalElements());
    }

    @Test
    void it_should_filter_by_negated_boolean_field() {
        var params = Map.of(
                "booleanField_n-eq", "true"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(TEST_SIZE, results.getTotalElements());
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
                "booleanField_null", "true"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(0, results.getTotalElements());
    }


}