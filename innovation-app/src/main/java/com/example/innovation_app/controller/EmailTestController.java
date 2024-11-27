package com.example.innovation_app.controller;

import com.example.innovation_app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    @Autowired
    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-test-email")
    public String sendTestEmail(@RequestParam String to) {
        try {
            emailService.sendEmail(
                    to,
                    "Test e-mail from Application",
                    "This is a test email sent from the application."
            );
            return "E-mail sent successfully to: " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending e-mail: " + e.getMessage();
        }
    }
}
