package com.xlr8code.server.common.service;

import com.xlr8code.server.common.helper.ApplicationLocaleResolver;
import com.xlr8code.server.common.enums.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LocaleServiceTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private ApplicationLocaleResolver localeResolver;

    private LocaleService localeService;

    static Stream<Arguments> messageParameters() {
        return Stream.of(
                Arguments.of(Language.BRAZILIAN_PORTUGUESE, "Teste de Mensagem"),
                Arguments.of(Language.AMERICAN_ENGLISH, "Test Message")
        );
    }

    @BeforeEach
    void setUp() {
        localeService = new LocaleService(messageSource, localeResolver);
    }

    @ParameterizedTest
    @MethodSource("messageParameters")
    void it_should_return_message_according_to_locale(Language language, String expectedMessage) {
        var locale = Locale.of(language.getCode());
        var LANGUAGE_HEADER = "Accept-Language";
        var TEST_MESSAGE_CODE = "test.message.code";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(LANGUAGE_HEADER, language.getCode());

        when(localeResolver.resolveLocale(request)).thenReturn(locale);
        when(messageSource.getMessage(TEST_MESSAGE_CODE, null, locale)).thenReturn(expectedMessage);

        var message = localeService.getMessage(TEST_MESSAGE_CODE, request);

        assertEquals(expectedMessage, message);
    }

    @Test
    void it_should_return_all_accepted_languages() {
        var acceptedLanguages = Set.of(Locale.of("en_US"), Locale.of("pt_BR"));
        var expectedLanguages = Set.of(Language.AMERICAN_ENGLISH, Language.BRAZILIAN_PORTUGUESE);

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(localeResolver.getAllAcceptedLocales(request)).thenReturn(acceptedLanguages);

        var languages = localeService.getAllAcceptedLanguages(request);

        assertEquals(expectedLanguages, languages);
    }

    @Test
    void it_should_return_language_according_to_locale() {
        var locale = Locale.of(Language.BRAZILIAN_PORTUGUESE.getCode());

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(localeResolver.resolveLocale(request)).thenReturn(locale);

        var language = localeService.resolveLanguage(request);

        assertEquals(Language.BRAZILIAN_PORTUGUESE, language);
    }

}