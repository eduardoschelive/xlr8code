package com.xlr8code.server.common.helper;

import com.xlr8code.server.common.utils.Language;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Component
public class ApplicationLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final Language[] SUPPORTED_LANGUAGES = Language.values();

    @Override
    @Nonnull
    public Locale resolveLocale(HttpServletRequest request) {
        var locale = request.getHeader(ACCEPT_LANGUAGE_HEADER);

        if (ObjectUtils.isEmpty(locale) || !isSupported(locale)) {
            return DEFAULT_LOCALE;
        }

        return Locale.of(locale);
    }

    private boolean isSupported(String locale) {
        var supportedLocales = List.of(SUPPORTED_LANGUAGES);
        return supportedLocales.stream().anyMatch(supportedLocale -> supportedLocale.getCode().equals(locale));
    }

}
