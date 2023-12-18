package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {


    @ParameterizedTest
    @ValueSource(strings = {"system", "light", "dark"})
    void itShouldReturnThemeFromCode(String themeCode) {
        var expectedTheme = Theme.valueOf(themeCode.toUpperCase());

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
