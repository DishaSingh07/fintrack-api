package com.disha.fintrack.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:NOT_SET}")
    private String username;

    @Value("${spring.mail.password:NOT_SET}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.from:NOT_SET}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void logMailConfig() {
        log.info("===== MAIL CONFIG CHECK =====");
        log.info("spring.mail.username = {}", username);
        log.info("spring.mail.password = {}", password.equals("NOT_SET") ? "NOT_SET" : "******");
        log.info("spring.mail.properties.mail.smtp.from = {}", fromEmail);
        log.info("=============================");
    }

    public void sendEmail(String to, String subject, String body) {
        log.info("Attempting to send email to {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        log.info("Email SENT successfully to {}", to);
    }
}
