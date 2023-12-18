package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StringUtilsTest {

    static Stream<Arguments> inputAndExpectedOutputProvider() {
        return Stream.of(
                Arguments.of("PascalCase", "Pascal Case"),
                Arguments.of("PascalCASE", "Pascal CASE"),
                Arguments.of("PascalCaseABC", "Pascal Case ABC"),
                Arguments.of("PascalCaseABCDef", "Pascal Case ABC Def")
        );
    }

    @ParameterizedTest
    @MethodSource("inputAndExpectedOutputProvider")
    void it_should_split_pascal_case(String input, String expectedOutput) {
        assertThat(StringUtils.splitPascalCase(input)).isEqualTo(expectedOutput);
    }

}
