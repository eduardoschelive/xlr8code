package com.xlr8code.server.common.service;

import com.xlr8code.server.common.interfaces.Mail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${mail.from}")
    private String from;

    @Async
    public void sendMail(Mail mail) {
        try {
            var message = javaMailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, true);
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject(messageSource));
            helper.setText(mail.getBody(templateEngine), true);
            helper.setFrom(from);

            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            log.error("Error while sending email ", e);
        }
    }

}
