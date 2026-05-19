package com.example.HotelManagementSystem.controller.user;

import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.service.PaymentService;

@RestController
@RequestMapping("/api/user/payments")
public class UserPaymentController {

    // =========================
    // Service Injection
    // =========================

    private final PaymentService paymentService;

    public UserPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    // =========================
    // CREATE PAYMENT
    // =========================

    @PostMapping
    public Payment createPayment(
            @RequestBody Payment payment) {

        return paymentService.createPayment(
                payment);
    }

    // =========================
    // CREATE RAZORPAY ORDER
    // =========================

    @PostMapping("/create-order/{paymentId}")
    public String createRazorpayOrder(
            @PathVariable Long paymentId) {

        return paymentService
                .createRazorpayOrder(
                        paymentId);
    }

    // =========================
    // VERIFY PAYMENT
    // =========================

    @PostMapping("/verify/{transactionId}")
    public Payment verifyPayment(
            @PathVariable String transactionId) {

        return paymentService
                .verifyPayment(
                        transactionId);
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
}