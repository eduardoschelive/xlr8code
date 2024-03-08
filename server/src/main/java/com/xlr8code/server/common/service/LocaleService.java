package com.xlr8code.server.common.service;

import com.xlr8code.server.common.helper.ApplicationLocaleResolver;
import com.xlr8code.server.common.utils.Language;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocaleService {

    private final MessageSource messageSource;
    private final ApplicationLocaleResolver localeResolver;

    /**
     * @param code    the code of the message to be retrieved
     * @param request the request from which the locale will be retrieved
     * @return the message corresponding to the given code in the locale of the given request
     */
    public String getMessage(String code, HttpServletRequest request) {
        return this.messageSource.getMessage(code, null, this.localeResolver.resolveLocale(request));
    }

    public Language resolveLanguage(HttpServletRequest request) {
        return Language.fromCode(this.localeResolver.resolveLocale(request).getLanguage());
    }

    public Set<Language> getAllAcceptedLanguages(HttpServletRequest request) {
        var allAcceptedLanguages = this.localeResolver.getAllAcceptedLocales(request);
        return allAcceptedLanguages.stream()
                .map(x -> Language.fromCode(x.getLanguage()))
                .collect(Collectors.toUnmodifiableSet());
    }

}
