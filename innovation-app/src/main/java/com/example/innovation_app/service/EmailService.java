package com.example.innovation_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Wysyła e-mail do pojedynczego odbiorcy.
     *
     * @param to      Adres e-mail odbiorcy
     * @param subject Temat wiadomości
     * @param text    Treść wiadomości
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inzynierka0010@gmail.com"); // Adres nadawcy
        message.setTo(to); // Adres odbiorcy
        message.setSubject(subject); // Temat wiadomości
        message.setText(text); // Treść wiadomości
        mailSender.send(message);
    }

    /**
     * Wysyła e-mail do wielu odbiorców.
     *
     * @param recipients Tablica adresów e-mail odbiorców
     * @param subject    Temat wiadomości
     * @param text       Treść wiadomości
     */
    public void sendEmailToMultipleRecipients(String[] recipients, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inzynierka0010@gmail.com"); // Adres nadawcy
        message.setTo(recipients); // Tablica odbiorców
        message.setSubject(subject); // Temat wiadomości
        message.setText(text); // Treść wiadomości
        mailSender.send(message);
    }
}
