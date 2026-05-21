package com.example.HotelManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.entity.PaymentStatus;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    // Find Payment By Transaction ID
    Optional<Payment> findByTransactionId(
            String transactionId);

    // Check Transaction Exists
    boolean existsByTransactionId(
            String transactionId);

    // Find Payment By Booking ID
    Optional<Payment> findByBookingId(
            Long bookingId);

    // Find Payments By Status
    List<Payment> findByPaymentStatus(
            PaymentStatus paymentStatus);
}