package com.xlr8code.server.common.interfaces;

import org.springframework.context.MessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

public interface Mail {

    String[] getTo();

    String getSubject(MessageSource messageSource);

    String getBody(TemplateEngine templateEngine, Context context);

    Locale getLocale();

    default String getMailPath(String template) {
        return "mail/" + template;
    }

}
