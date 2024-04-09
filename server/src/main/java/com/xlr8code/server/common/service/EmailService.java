package com.xlr8code.server.common.service;

import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${mail.from}")
    private String from;

    public void sendEmail(String email, String subject, String body) {
        log.info("Sending email to {} with subject: {} and body: {}", email, subject, body);

        try {
            var message = javaMailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(from);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error while sending email" );
            e.printStackTrace();
        }
    }

    public void sendActivationEmail(User user, String activationCode) {
        var template = "user-activation";



        var subject = "Account activation";

        var email = user.getEmail();

        var context = new Context();
        context.setVariable("name", user.getUsername());
        context.setVariable("activationCode", activationCode);

        var body = templateEngine.process(template, context);


        this.sendEmail(email, subject, body);
    }

    public void sendPasswordResetEmail(String email, String passwordResetCode) {
        var subject = "Password reset";
        var body = "Your currentPassword reset code is " + passwordResetCode;

        this.sendEmail(email, subject, body);
    }

}
