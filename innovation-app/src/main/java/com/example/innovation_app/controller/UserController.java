package com.example.innovation_app.controller;

import com.example.innovation_app.model.User;
import com.example.innovation_app.repository.SubmissionRepository;
import com.example.innovation_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // Kodowanie hasła użytkownika
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Przypisanie menedżera (jeśli podano)
        if (user.getManager() != null && user.getManager().getId() != null) {
            user.setManager(userRepository.findById(user.getManager().getId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono menedżera o ID: " + user.getManager().getId())));
        }

        userRepository.save(user);
        return ResponseEntity.ok("Użytkownik dodany pomyślnie");
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    // Aktualizacja danych użytkownika
                    user.setUsername(updatedUser.getUsername());
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPhoneNumber(updatedUser.getPhoneNumber());
                    user.setRole(updatedUser.getRole());

                    // Aktualizacja menedżera
                    if (updatedUser.getManager() != null && updatedUser.getManager().getId() != null) {
                        user.setManager(userRepository.findById(updatedUser.getManager().getId())
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono menedżera o ID: " + updatedUser.getManager().getId())));
                    } else {
                        user.setManager(null); // Usunięcie menedżera
                    }

                    userRepository.save(user);
                    return ResponseEntity.ok("Dane użytkownika zaktualizowane pomyślnie");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie znaleziony"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            // Ustawienie userId na NULL dla wszystkich zgłoszeń przypisanych do użytkownika
            submissionRepository.setUserIdToNullForUser(id);

            // Usunięcie użytkownika
            userRepository.deleteById(id);
            return ResponseEntity.ok("Użytkownik usunięty pomyślnie");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie znaleziony");
        }
    }
    @GetMapping
    public List<User> getUsersByRole(@RequestParam(required = false) User.Role role) {
        if (role != null) {
            return userRepository.findByRole(role);
        }
        return userRepository.findAll();
    }
}

