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

class LanguageTest {

    static Stream<Arguments> inputAndExpectedOutputProvider() {
        return Stream.of(
                Arguments.of("pt_BR", Language.BRAZILIAN_PORTUGUESE),
                Arguments.of("en_US", Language.AMERICAN_ENGLISH)
        );
    }

    @Test
    void itShouldBePresentOnInputAndExpectedOutputProvider() {
        var languages = Language.values();

        var isPresent = Stream.of(languages)
                .allMatch(language -> inputAndExpectedOutputProvider()
                        .anyMatch(arguments -> arguments.get()[0].equals(language.getCode())));

        assertThat(isPresent).isTrue();
    }

    @ParameterizedTest
    @MethodSource("inputAndExpectedOutputProvider")
    void itShouldReturnLanguageFromCode(String languageCode, Language expectedLanguage) {

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
