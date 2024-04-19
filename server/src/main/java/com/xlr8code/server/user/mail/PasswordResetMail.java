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
public class PasswordResetMail implements Mail {
    private static final String TEMPLATE = "password-recovery";
    private static final String SUBJECT_KEY = "mail.account.recovery.subject";

    private final String[] to;

    private final String passwordResetCode;
    private final String passwordResetUrl;
    private final String username;

    private final Locale locale;

    @Override
    public String getSubject(MessageSource messageSource) {
        return messageSource.getMessage(SUBJECT_KEY, null, locale);
    }

    public String getBody(TemplateEngine templateEngine, Context context) {
        context.setVariable("username", username);
        context.setVariable("passwordResetCode", passwordResetCode);
        context.setVariable("passwordResetUrl", passwordResetUrl);
        return templateEngine.process(TEMPLATE, context);
    }

}
