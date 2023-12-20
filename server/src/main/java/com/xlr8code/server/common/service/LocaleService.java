package com.xlr8code.server.common.service;

import com.xlr8code.server.common.helper.ApplicationLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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

}
