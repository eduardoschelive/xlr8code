package com.xlr8code.server.common.interceptor;

import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.service.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

@RequiredArgsConstructor
public class ContentLanguageInterceptor implements HandlerInterceptor {

    private static final String CONTENT_LANGUAGE_HEADER = "Content-Language";
    private static final String LANGUAGE_SEPARATOR = ", ";
    private final LocaleService localeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var handlerMethod = (HandlerMethod) handler;
        var method = handlerMethod.getMethod();

        var headerValue = this.getHeaderValue(method, request);
        response.setHeader(CONTENT_LANGUAGE_HEADER, headerValue);

        return true;
    }


    private String getHeaderValue(Method method, HttpServletRequest request) {
        var isMultiLanguage = method.isAnnotationPresent(MultiLanguageContent.class);
        if (!isMultiLanguage) {
            var resolvedLanguage = this.localeService.resolveLanguage(request);
            return resolvedLanguage.getCode();
        } else {
            var acceptedLanguages = this.localeService.getAllAcceptedLanguages(request);
            return this.buildHeaderValue(acceptedLanguages);
        }
    }

    private String buildHeaderValue(Set<Language> acceptedLanguages) {
        var sb = new StringBuilder();

        for (var language : acceptedLanguages) {
            sb.append(language.getCode());
            sb.append(LANGUAGE_SEPARATOR);
        }
        sb.delete(sb.length() - LANGUAGE_SEPARATOR.length(), sb.length());

        return sb.toString();
    }

}
