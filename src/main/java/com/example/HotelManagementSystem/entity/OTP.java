package com.example.HotelManagementSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "otps")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Email
    // =========================

    @Column(nullable = false)
    private String email;

    // =========================
    // OTP Code
    // =========================

    @Column(nullable = false)
    private String code;

    // =========================
    // Expiration Time
    // =========================

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    // =========================
    // Used Status
    // =========================

    @Column(nullable = false)
    private Boolean isUsed = false;

    // =========================
    // Created At
    // =========================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // =========================
    // Constructors
    // =========================

    public OTP() {
    }

    public OTP(String email, String code, LocalDateTime expiryTime) {
        this.email = email;
        this.code = code;
        this.expiryTime = expiryTime;
        this.createdAt = LocalDateTime.now();
        this.isUsed = false;
    }

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
