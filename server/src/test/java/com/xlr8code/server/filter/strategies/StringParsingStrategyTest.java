package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.common.enums.Theme;
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

    @Autowired
    private FilterTestRepository testRepository;

    @BeforeAll
    static void setup(@Autowired FilterTestRepository repository) {
        var entity = FilterTestEntity.builder()
                .stringField("stringField")
                .booleanField(true)
                .enumThemeField(Theme.LIGHT)
                .build();

        repository.save(entity);
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
                Arguments.of(Map.of("stringField_eq", "stringField"), 1),
                Arguments.of(Map.of("stringField_eq-i", "STRINGFIELD"), 1),
                Arguments.of(Map.of("stringField_n-eq", "stringField"), 0),
                Arguments.of(Map.of("stringField_n-eq-i", "STRINGFIELD"), 0),
                Arguments.of(Map.of("stringField_sw", "stringFie"), 1),
                Arguments.of(Map.of("stringField_sw-i", "STRINGFIE"), 1),
                Arguments.of(Map.of("stringField_ew", "eld"), 1),
                Arguments.of(Map.of("stringField_ew-i", "ELD"), 1),
                Arguments.of(Map.of("stringField_lk", "stringField"), 1),
                Arguments.of(Map.of("stringField_in", "stringField,stringField1"), 1)
        );
    }
}