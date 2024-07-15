package com.xlr8code.server.common.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApplicationLocaleResolverTest {

    private ApplicationLocaleResolver localeResolver;

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                arguments(null, Locale.of("en_US")), // Test case for resolving locale without header
                arguments("invalid_locale_format", Locale.of("en_US")), // Test case for invalid locale format
                arguments("pt_BR", Locale.of("pt_BR")), // Test case for resolving locale with header
                arguments("en_US;q=0.8,pt_BR;q=0.9", Locale.of("pt_BR")), // Test case for resolving locale with quality score
                arguments("en_US;q=0.8,pt_BR", Locale.of("pt_BR")), // Test case for resolving locale with quality score and default
                arguments("en-US", Locale.of("en_US")), // Test case for resolving locale with dash
                arguments("pt-BR", Locale.of("pt_BR")) // Test case for resolving locale with dash
        );
    }

    @BeforeEach
    void setUp() {
        localeResolver = new ApplicationLocaleResolver();
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void it_should_resolve_locale(String acceptLanguageHeader, Locale expectedLocale) {
        var request = new MockHttpServletRequest();
        if (acceptLanguageHeader != null) {
            request.addHeader("Accept-Language", acceptLanguageHeader);
        }

        var resolvedLocale = localeResolver.resolveLocale(request);

        assertEquals(expectedLocale, resolvedLocale);
    }

    @Test
    void it_should_resolve_all_accepted_locales() {
        var request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "en_US;q=0.8,pt_BR;q=0.9");

        var resolvedLocales = localeResolver.getAllAcceptedLocales(request);

        assertEquals(Set.of(Locale.of("en_US"), Locale.of("pt_BR")), resolvedLocales);
    }

    @Test
    void it_should_resolve_to_default_locale_when_request_uri_is_in_ignore_paths() {
        var request = new MockHttpServletRequest();
        request.setRequestURI("/v3/api-docs");

        var resolvedLocale = localeResolver.resolveLocale(request);

        assertEquals(Locale.of("en_US"), resolvedLocale);
    }

}