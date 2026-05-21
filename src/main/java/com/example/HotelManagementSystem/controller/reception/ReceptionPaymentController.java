package com.example.HotelManagementSystem.controller.reception;

import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.dto.CashPaymentRequest;
import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.service.PaymentService;

@RestController
@RequestMapping("/api/reception/payments")
public class ReceptionPaymentController {

    private final PaymentService paymentService;

    public ReceptionPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    // =========================
    // CASH PAYMENT AT RECEPTION
    // =========================

    @PutMapping("/cash/{bookingId}")
    public Payment receiveCashPayment(
            @PathVariable Long bookingId,
            @RequestBody CashPaymentRequest request) {

        return paymentService.receiveCashPayment(
                bookingId,
                request);
    }
}