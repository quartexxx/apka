package com.example.innovation_app.controller;

import com.example.innovation_app.model.InnovationSubmission;
import com.example.innovation_app.model.User;
import com.example.innovation_app.repository.SubmissionRepository;
import com.example.innovation_app.repository.UserRepository;
import com.example.innovation_app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/submit")
    public InnovationSubmission submitNew(@RequestBody InnovationSubmission submission) {
        // Ustawienie ID użytkownika (przykładowe)
        Long userId = 10031L; // Domyślne ID użytkownika (do testów)
        submission.setUserId(userId);

        // Ustaw dane zgłoszenia
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setStatus(InnovationSubmission.SubmissionStatus.PENDING);

        // Znajdź menedżera użytkownika
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        User manager = user.getManager(); // Znajdź przypisanego menedżera

        // Zapisz zgłoszenie w bazie
        InnovationSubmission savedSubmission = submissionRepository.save(submission);

        // Wyślij e-mail do menedżera
        if (manager != null) {
            emailService.sendEmail(
                    manager.getEmail(),
                    "Nowe zgłoszenie od użytkownika: " + user.getFirstName() + " " + user.getLastName(),
                    "Otrzymano nowe zgłoszenie. Szczegóły zgłoszenia: " + submission.getTitle()
            );
        } else {
            System.out.println("Brak przypisanego menedżera dla użytkownika: " + userId);
        }

        return savedSubmission;
    }

    @GetMapping("/all")
    public List<InnovationSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    private void sendNotificationEmails(InnovationSubmission submission) {
        List<String> recipients = new ArrayList<>();

        // Pobierz e-mail użytkownika zgłaszającego
        userRepository.findById(submission.getUserId())
                .ifPresent(user -> recipients.add(user.getEmail()));

        // Znajdź przełożonego użytkownika (np. pierwszego użytkownika o roli MANAGER)
        userRepository.findByRole(User.Role.MANAGER).stream()
                .findFirst()
                .ifPresent(manager -> recipients.add(manager.getEmail()));


        // Pobierz e-maile dodatkowych użytkowników
        if (submission.getAdditionalUsersIds() != null) {
            userRepository.findAllById(submission.getAdditionalUsersIds())
                    .forEach(user -> recipients.add(user.getEmail()));
        }

        // Pobierz e-mail dodatkowego odbiorcy
        if (submission.getAdditionalRecipientId() != null) {
            userRepository.findById(submission.getAdditionalRecipientId())
                    .ifPresent(user -> recipients.add(user.getEmail()));
        }

        // Wysyłanie e-maila, jeśli lista odbiorców nie jest pusta
        if (!recipients.isEmpty()) {
            String subject = "Nowe zgłoszenie innowacji: " + submission.getTitle();
            String text = "Szczegóły zgłoszenia:\n\n" +
                    "Tytuł: " + submission.getTitle() + "\n" +
                    "Opis: " + submission.getDescription() + "\n\n" +
                    "Sprawdź szczegóły w aplikacji.";
            emailService.sendEmailToMultipleRecipients(recipients.toArray(new String[0]), subject, text);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id) {
        InnovationSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zgłoszenie nie znalezione"));

        if (submission.getStatus() != InnovationSubmission.SubmissionStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Nie można usunąć zgłoszenia, które jest przetwarzane, zaakceptowane lub odrzucone.");
        }

        submissionRepository.delete(submission);
        return ResponseEntity.ok("Zgłoszenie zostało usunięte");
    }
}
