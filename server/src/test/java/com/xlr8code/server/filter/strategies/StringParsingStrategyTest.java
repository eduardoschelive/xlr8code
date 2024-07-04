package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StringParsingStrategyTest {

    private static final int TEST_SIZE = 1;

    @Autowired
    private FilterTestRepository testRepository;

    @BeforeAll
    static void setup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.createNEntities(TEST_SIZE, "stringField", true);
    }

    @AfterAll
    static void cleanup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.clearRepositories();
    }

    @ParameterizedTest
    @MethodSource("provideFilterParameters")
    void it_should_filter_with_string_field(Map<String, String> params, long expectedSize) {
        var results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(expectedSize, results.getTotalElements());
    }

    private static Stream<Arguments> provideFilterParameters() {
        return Stream.of(
                Arguments.of(Map.of("stringField_eq", "stringField0"), TEST_SIZE),
                Arguments.of(Map.of("stringField_eq-i", "STRINGFIELD0"), TEST_SIZE),
                Arguments.of(Map.of("stringField_n-eq", "stringField0"), TEST_SIZE - 1),
                Arguments.of(Map.of("stringField_n-eq-i", "STRINGFIELD0"), TEST_SIZE - 1),
                Arguments.of(Map.of("stringField_sw", "stringField"), TEST_SIZE),
                Arguments.of(Map.of("stringField_sw-i", "STRINGFIELD"), TEST_SIZE),
                Arguments.of(Map.of("stringField_ew", "0"), TEST_SIZE),
                Arguments.of(Map.of("stringField_ew-i", "0"), TEST_SIZE),
                Arguments.of(Map.of("stringField_lk", "stringField0"), TEST_SIZE),
                Arguments.of(Map.of("stringField_in", "stringField0,stringField1"), TEST_SIZE)
        );
    }
}