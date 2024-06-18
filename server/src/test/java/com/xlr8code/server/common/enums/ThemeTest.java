package com.xlr8code.server.common.enums;

import com.xlr8code.server.user.exception.ThemeNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ThemeTest {

    static Stream<Arguments> inputAndExpectedOutputProvider() {
        return Stream.of(
                Arguments.of("system", Theme.SYSTEM),
                Arguments.of("light", Theme.LIGHT),
                Arguments.of("dark", Theme.DARK)
        );
    }

    @Test
    void it_should_be_present_on_input_and_expected_output_provider() {
        var themes = Theme.values();

        var isPresent = Stream.of(themes)
                .allMatch(theme -> inputAndExpectedOutputProvider()
                        .anyMatch(arguments -> arguments.get()[0].equals(theme.getCode())));

        assertThat(isPresent).isTrue();
    }

    @ParameterizedTest
    @MethodSource("inputAndExpectedOutputProvider")
    void it_should_return_theme_from_code(String themeCode, Theme expectedTheme) {
        var actualTheme = Theme.fromCode(themeCode);

        assertThat(actualTheme).isEqualTo(expectedTheme);
    }

    @Test
    void it_should_thrown_application_error_when_invalid_code() {
        var invalidThemeCode = "invalid";

        assertThatThrownBy(() -> Theme.fromCode(invalidThemeCode))
                .isInstanceOf(ThemeNotFoundException.class);

    }

}
