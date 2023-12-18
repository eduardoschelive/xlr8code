package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
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
class LanguageTest {

    static Stream<Arguments> languageParameters() {
        return Stream.of(
                Arguments.of("pt_BR", Language.BRAZILIAN_PORTUGUESE),
                Arguments.of("en_US", Language.AMERICAN_ENGLISH)
        );
    }

    @Test
    void it_should_have_all_languages_covered_on_test() {
        var languages = Language.values();

        var isPresent = Stream.of(languages)
                .allMatch(language -> languageParameters()
                        .anyMatch(arguments -> arguments.get()[0].equals(language.getCode())));

        assertThat(isPresent).isTrue();
    }

    @ParameterizedTest
    @MethodSource("languageParameters")
    void it_should_return_language_from_code(String languageCode, Language expectedLanguage) {

        var actualLanguage = Language.fromCode(languageCode);

        assertThat(actualLanguage).isEqualTo(expectedLanguage);

    }

    @Test
    void it_should_throw_application_error_when_receive_a_invalid_code() {
        var invalidLanguageCode = "invalid";

        assertThatThrownBy(() -> Language.fromCode(invalidLanguageCode))
                .isInstanceOf(ApplicationException.class)
                .hasFieldOrPropertyWithValue("exceptionType", CommonExceptionType.LANGUAGE_NOT_FOUND);
    }

}
