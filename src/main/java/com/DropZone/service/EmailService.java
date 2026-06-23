package com.DropZone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("DropZone – Reset Your Password");
        message.setText(
                "You requested a password reset for your DropZone account.\n\n" +
                "Click the link below to reset your password. This link expires in 30 minutes.\n\n" +
                resetLink + "\n\n" +
                "If you did not request this, you can safely ignore this email."
        );

        mailSender.send(message);
    }
}
