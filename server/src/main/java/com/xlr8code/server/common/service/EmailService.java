package com.xlr8code.server.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String email, String subject, String body) {
        log.info("Sending email to {} with subject: {} and body: {}", email, subject, body);

        try {
            var message = javaMailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("mail@xlr8code.com");

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error while sending email" );
            e.printStackTrace();
        }
    }

    public void sendActivationEmail(String email, String activationCode) {
        var subject = "Account activation";
        var body = "Your activation code is " + activationCode;

        this.sendEmail(email, subject, body);
    }

    public void sendPasswordResetEmail(String email, String passwordResetCode) {
        var subject = "Password reset";
        var body = "Your currentPassword reset code is " + passwordResetCode;

        this.sendEmail(email, subject, body);
    }

}
