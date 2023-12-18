package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LanguageTest {

    @ParameterizedTest
    @ValueSource(strings = {"pt_BR", "en_US"})
    void itShouldReturnLanguageFromCode(String languageCode) {
        var expectedLanguage = Language.valueOf(languageCode.toUpperCase());

        var actualLanguage = Language.fromCode(languageCode);

        assertThat(actualLanguage).isEqualTo(expectedLanguage);
    }

    @Test
    void itShouldThrownApplicationExceptionWhenLanguageCodeIsInvalid() {
        var invalidLanguageCode = "invalid";

        assertThatThrownBy(() -> Language.fromCode(invalidLanguageCode))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue("exceptionType", CommonExceptionType.LANGUAGE_NOT_FOUND);
    }

}
