package com.example.HotelManagementSystem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OTPVerificationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP code is required")
    private String code;

    // =========================
    // Constructors
    // =========================

    public OTPVerificationRequest() {
    }

    public OTPVerificationRequest(String email, String code) {
        this.email = email;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
