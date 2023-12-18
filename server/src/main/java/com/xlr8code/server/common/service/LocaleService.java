package com.xlr8code.server.common.service;

import com.xlr8code.server.common.helper.ApplicationLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleService {

    private final MessageSource messageSource;
    private final ApplicationLocaleResolver localeResolver;

    public String getMessage(String code, HttpServletRequest request) {
        return this.messageSource.getMessage(code, null, this.localeResolver.resolveLocale(request));
    }

}
