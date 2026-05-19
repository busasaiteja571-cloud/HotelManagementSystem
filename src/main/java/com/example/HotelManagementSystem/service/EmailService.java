package com.example.HotelManagementSystem.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // =========================
    // Mail Sender Injection
    // =========================

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================
    // SEND EMAIL
    // =========================

    public void sendEmail(
            String to,
            String subject,
            String body) {

        try {

            // Create Email Message
            SimpleMailMessage message =
                    new SimpleMailMessage();

            // Set Receiver Email
            message.setTo(to);

            // Set Subject
            message.setSubject(subject);

            // Set Email Body
            message.setText(body);

            // Send Email
            mailSender.send(message);

            System.out.println(
                    "Email sent successfully");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send email");
        }
    }
}