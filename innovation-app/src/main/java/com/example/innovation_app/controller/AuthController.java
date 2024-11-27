package com.example.innovation_app.controller;

import com.example.innovation_app.DTO.UserDetailsDTO;
import com.example.innovation_app.model.User;
import com.example.innovation_app.payload.LoginRequest;
import com.example.innovation_app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Tworzymy obiekt UserDetailsDTO do zwrócenia
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString()
        );

        return ResponseEntity.ok(userDetailsDTO);
    }
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user");
        }

        // Sprawdź, czy Principal to UserDetails
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid user details");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString()
        );
        return ResponseEntity.ok(userDetailsDTO);
    }

}
