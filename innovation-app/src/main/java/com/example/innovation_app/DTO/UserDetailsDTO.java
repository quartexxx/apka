package com.example.innovation_app.DTO;

public class UserDetailsDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String role;

    // Konstruktor bezargumentowy (przydatny np. dla frameworków do serializacji)
    public UserDetailsDTO() {
    }

    // Konstruktor pełny
    public UserDetailsDTO(String username, String firstName, String lastName, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Gettery i Settery
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
