package com.example.innovation_app.payload;

public class LoginRequest {
    private String username;
    private String password;

    // Gettery i Settery
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
