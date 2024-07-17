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
    private final String template = getMailPath("account-activation");
    private final String subjectKey = "mail.account.activation.subject";

    private final String[] to;

    private final String activationCode;
    private final String activationUrl;
    private final String username;

    private final Locale locale;

    @Override
    public String getSubject(MessageSource messageSource) {
        return messageSource.getMessage(subjectKey, null, locale);
    }

    @Override
    public String getBody(TemplateEngine templateEngine, Context context) {
        context.setVariable("username", username);
        context.setVariable("activationCode", activationCode);
        context.setVariable("activationUrl", activationUrl);
        return templateEngine.process(template, context);
    }

}
