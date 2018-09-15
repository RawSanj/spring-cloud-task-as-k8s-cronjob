package com.github.rawsanj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService {


    private final static Logger LOGGER = LoggerFactory.getLogger(EmailNotificationService.class.getName());

    private final JavaMailSender emailSender;

    public EmailNotificationService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {

        LOGGER.info("Sending Email Notification to {} with Subject", to, subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        System.out.close();

        try {
            emailSender.send(message);
        } catch (MailException ex) {
            LOGGER.error("Email or Password is incorrectly configured");
            throw new RuntimeException("Message: " + ex.getMessage() + ". Caused by: " + ex.getCause().getMessage());
        }
    }

}
