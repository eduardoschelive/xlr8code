package com.xlr8code.server.common.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String email, String subject, String body) {
        // TODO: Implement email sending and remove logging
        System.out.println("Email sent to " + email + " with subject " + subject + " and body " + body);
    }

    public void sendActivationEmail(String email, String activationCode) {
        var subject = "Account activation";
        var body = "Your activation code is " + activationCode;

        this.sendEmail(email, subject, body);
    }

    public void sendPasswordResetEmail(String email, String passwordResetCode) {
        var subject = "Password reset";
        var body = "Your password reset code is " + passwordResetCode;

        this.sendEmail(email, subject, body);
    }

}
