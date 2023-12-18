package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    static Stream<Arguments> inputAndExpectedOutputProvider() {
        return Stream.of(
                Arguments.of("system", Theme.SYSTEM),
                Arguments.of("light", Theme.LIGHT),
                Arguments.of("dark", Theme.DARK)
        );
    }

    @ParameterizedTest
    @MethodSource("inputAndExpectedOutputProvider")
    void itShouldReturnThemeFromCode(String themeCode, Theme expectedTheme) {
        var actualTheme = Theme.fromCode(themeCode);

        assertThat(actualTheme).isEqualTo(expectedTheme);
    }

    @Test
    void itShouldThrowApplicationExceptionWhenThemeCodeIsInvalid() {
        var invalidThemeCode = "invalid";

        assertThatThrownBy(() -> Theme.fromCode(invalidThemeCode))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue("exceptionType", CommonExceptionType.THEME_NOT_FOUND);

    }

}
