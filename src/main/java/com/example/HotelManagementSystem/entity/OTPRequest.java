package com.example.HotelManagementSystem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OTPRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // =========================
    // Constructors
    // =========================

    public OTPRequest() {
    }

    public OTPRequest(String email) {
        this.email = email;
    }

    // =========================
    // Getters and Setters
    // =========================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
