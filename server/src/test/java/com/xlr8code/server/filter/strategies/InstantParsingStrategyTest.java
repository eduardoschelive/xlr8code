package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.exception.RequiredParamSizeException;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InstantParsingStrategyTest {

    private static final Instant INSTANT = Instant.parse("2021-08-01T00:00:00Z");

    @Autowired
    private FilterTestRepository testRepository;

    @Autowired
    private FilterTestUtils filterTestUtils;

    @BeforeAll
    static void setup(@Autowired FilterTestRepository repository) {
        var entity = FilterTestEntity.builder()
                .stringField("stringField")
                .booleanField(true)
                .enumThemeField(Theme.LIGHT)
                .instantField(INSTANT)
                .build();

        repository.save(entity);
    }

    @AfterAll
    static void cleanup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.clearRepositories();
    }

    private static Stream<Arguments> provideFilterParameters() {
        var instantString = INSTANT.toString();
        var upperBound = INSTANT.plusSeconds(1).toString();
        var lowerBound = INSTANT.minusSeconds(1).toString();

        return Stream.of(
                Arguments.of(Map.of("instantField_eq", instantString), 1),
                Arguments.of(Map.of("instantField_n-eq", instantString), 0),
                Arguments.of(Map.of("instantField_gt", instantString), 0),
                Arguments.of(Map.of("instantField_gte", instantString), 1),
                Arguments.of(Map.of("instantField_lt", instantString), 0),
                Arguments.of(Map.of("instantField_lte", instantString), 1),
                Arguments.arguments(Map.of("instantField_btw", lowerBound + "," + upperBound), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterParameters")
    void it_should_filter_with_instant_field(Map<String, String> params, int expected) {

        var spec = filterTestUtils.buildSpecification(params);
        var results = testRepository.findAll(spec);

        assertEquals(expected, results.size());
    }

    @Test
    void it_should_return_error_when_between_does_not_have_two_values() {
        var params = Map.of("instantField_btw", "2021-08-01T00:00:00Z");
        var spec = filterTestUtils.buildSpecification(params);

         assertThrows(RequiredParamSizeException.class, () -> testRepository.findAll(spec));
    }


}