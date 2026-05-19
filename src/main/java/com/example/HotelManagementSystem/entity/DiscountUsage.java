package com.example.HotelManagementSystem.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "discount_usages")
public class DiscountUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Discount Reference
    // =========================

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    @JsonBackReference
    private Discount discount;

    // =========================
    // User Reference
    // =========================

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =========================
    // Booking Reference
    // =========================

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // =========================
    // Discount Amount Applied
    // =========================

    @Column(nullable = false)
    private Double discountAmountApplied;

    // =========================
    // Original Amount
    // =========================

    @Column(nullable = false)
    private Double originalAmount;

    // =========================
    // Final Amount After Discount
    // =========================

    @Column(nullable = false)
    private Double finalAmount;

    // =========================
    // Used At
    // =========================

    @Column(nullable = false, updatable = false)
    private LocalDateTime usedAt;

    // =========================
    // Constructors
    // =========================

    public DiscountUsage() {
    }

    public DiscountUsage(
            Discount discount,
            User user,
            Double discountAmountApplied,
            Double originalAmount,
            Double finalAmount) {
        this.discount = discount;
        this.user = user;
        this.discountAmountApplied = discountAmountApplied;
        this.originalAmount = originalAmount;
        this.finalAmount = finalAmount;
    }

    // =========================
    // Lifecycle
    // =========================

    @PrePersist
    protected void onCreate() {
        usedAt = LocalDateTime.now();
    }

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Double getDiscountAmountApplied() {
        return discountAmountApplied;
    }

    public void setDiscountAmountApplied(Double discountAmountApplied) {
        this.discountAmountApplied = discountAmountApplied;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
