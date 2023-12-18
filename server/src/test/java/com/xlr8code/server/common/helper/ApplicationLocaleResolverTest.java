package com.xlr8code.server.common.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApplicationLocaleResolverTest {

    private ApplicationLocaleResolver localeResolver;

    @BeforeEach
    void setUp() {
        localeResolver = new ApplicationLocaleResolver();
    }

    @Test
    void it_should_return_default_if_invalid_locale_format() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "invalid_locale_format");

        Locale resolvedLocale = localeResolver.resolveLocale(request);

        assertEquals(Locale.ENGLISH, resolvedLocale);
    }

    @Test
    void it_should_resolve_locale_with_header() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "pt_BR");

        Locale resolvedLocale = localeResolver.resolveLocale(request);

        assertEquals(Locale.of("pt_BR"), resolvedLocale);
    }

    @Test
    void it_should_resolve_locale_without_header() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Locale resolvedLocale = localeResolver.resolveLocale(request);

        assertEquals(Locale.ENGLISH, resolvedLocale);
    }

}