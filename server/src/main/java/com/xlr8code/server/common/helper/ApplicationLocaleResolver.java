package com.xlr8code.server.common.helper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Component
public class ApplicationLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Override
    @Nonnull
    public Locale resolveLocale(HttpServletRequest request) {
        var locale = request.getHeader(ACCEPT_LANGUAGE_HEADER);
        return ObjectUtils.isEmpty(locale) ? DEFAULT_LOCALE : Locale.of(locale);
    }

}
