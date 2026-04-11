package com.app.java.WebAppJava.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLoginCode(String email, String code, int ttlMinutes) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(email);
        msg.setSubject("Код входа в Java Learning");
        msg.setText(
                "Ваш код входа: " + code + "\n\n" +
                        "Он действует " + ttlMinutes + " минут.\n" +
                        "Если это были не вы — просто игнорируйте письмо."
        );
        mailSender.send(msg);
    }
}