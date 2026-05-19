package com.example.HotelManagementSystem.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.entity.PaymentStatus;
import com.example.HotelManagementSystem.service.PaymentService;

@RestController
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    // =========================
    // Service Injection
    // =========================

    private final PaymentService paymentService;

    public AdminPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================

    @GetMapping
    public List<Payment> getAllPayments() {

        return paymentService
                .getAllPayments();
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================

    @GetMapping("/{id}")
    public Payment getPaymentById(
            @PathVariable Long id) {

        return paymentService
                .getPaymentById(id);
    }

    // =========================
    // GET PAYMENTS BY STATUS
    // =========================

    @GetMapping("/status/{status}")
    public List<Payment> getPaymentsByStatus(
            @PathVariable PaymentStatus status) {

        return paymentService
                .getPaymentsByStatus(
                        status);
    }

    // =========================
    // REFUND PAYMENT
    // =========================

    @PutMapping("/refund/{paymentId}")
    public Payment refundPayment(
            @PathVariable Long paymentId) {

        return paymentService
                .refundPayment(
                        paymentId);
    }

    // =========================
    // DELETE PAYMENT
    // =========================

    @DeleteMapping("/{id}")
    public void deletePayment(
            @PathVariable Long id) {

        paymentService.deletePayment(id);
    }
}