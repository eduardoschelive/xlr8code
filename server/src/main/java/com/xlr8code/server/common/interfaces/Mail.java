package com.xlr8code.server.common.interfaces;

import org.springframework.context.MessageSource;
import org.thymeleaf.TemplateEngine;

public interface Mail {

    String[] getTo();
    String getSubject(MessageSource messageSource);
    String getBody(TemplateEngine templateEngine);

}
