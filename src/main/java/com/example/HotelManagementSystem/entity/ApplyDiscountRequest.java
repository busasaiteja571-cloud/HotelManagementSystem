package com.example.HotelManagementSystem.entity;

import jakarta.validation.constraints.NotBlank;

public class ApplyDiscountRequest {

    @NotBlank(message = "Discount code is required")
    private String discountCode;

    @NotBlank(message = "User ID is required")
    private Long userId;

    private Double bookingAmount;

    // =========================
    // Constructors
    // =========================

    public ApplyDiscountRequest() {
    }

    public ApplyDiscountRequest(
            String discountCode,
            Long userId,
            Double bookingAmount) {
        this.discountCode = discountCode;
        this.userId = userId;
        this.bookingAmount = bookingAmount;
    }

    // =========================
    // Getters and Setters
    // =========================

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(Double bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
}
