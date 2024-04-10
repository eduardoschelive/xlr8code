package com.xlr8code.server.user.mail;

import com.xlr8code.server.common.interfaces.Mail;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Getter
@Builder
public class AccountActivationMail implements Mail {
    private static final String TEMPLATE = "user-activation";
    private static final String SUBJECT_KEY = "mail.account.activation.subject";

    private final String[] to;

    private final String activationLink;
    private final String activationCode;
    private final String activationCodeUrl;
    private final String username;

    private final Locale locale;

    @Override
    public String getSubject(MessageSource messageSource) {
        return messageSource.getMessage(SUBJECT_KEY, null, locale);
    }

    @Override
    public String getBody(TemplateEngine templateEngine) {
        var context = new Context(locale);
        context.setVariable("username", username);
        context.setVariable("activationLink", activationLink);
        context.setVariable("activationCode", activationCode);
        context.setVariable("activationCodeUrl", activationCodeUrl);
        return templateEngine.process(TEMPLATE, context);
    }

}
