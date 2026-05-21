package com.example.HotelManagementSystem.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.dto.CashPaymentRequest;
import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.entity.PaymentMethod;
import com.example.HotelManagementSystem.entity.PaymentStatus;
import com.example.HotelManagementSystem.entity.BookingStatus;
import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.repository.BookingRepository;
import com.example.HotelManagementSystem.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class PaymentService {

    // =========================
    // Razorpay Keys
    // =========================

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.secret.key}")
    private String razorpaySecret;

    // =========================
    // Repository Injection
    // =========================

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    // =========================
    // CREATE PAYMENT
    // =========================

    public Payment createPayment(Payment payment) {

        // Check Booking Exists
        Booking booking = bookingRepository.findById(
                payment.getBooking().getId())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        // Validate Amount
        if (payment.getAmount() <= 0) {

            throw new RuntimeException(
                    "Amount must be greater than 0");
        }

        // Set Booking
        payment.setBooking(booking);

        // Initial Payment Status
        payment.setPaymentStatus(
                PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    // =========================
    // CREATE RAZORPAY ORDER
    // =========================

    public String createRazorpayOrder(
            Long paymentId) {

        try {

            Payment payment =
                    getPaymentById(paymentId);

            // Razorpay Client
            RazorpayClient razorpay =
                    new RazorpayClient(
                            razorpayKey,
                            razorpaySecret);

            // Create JSON Request
            JSONObject orderRequest =
                    new JSONObject();

            // Amount In Paise
            orderRequest.put(
                    "amount",
                    payment.getAmount() * 100);

            orderRequest.put(
                    "currency",
                    "INR");

            orderRequest.put(
                    "receipt",
                    "txn_" + payment.getId());

            // Create Razorpay Order
            Order order =
                    razorpay.orders.create(
                            orderRequest);

            // Save Transaction ID
            payment.setTransactionId(
                    order.get("id"));

            paymentRepository.save(payment);

            return order.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Razorpay order");
        }
    }

    // =========================
    // VERIFY PAYMENT
    // =========================

    public Payment verifyPayment(
            String transactionId) {

        Payment payment =
                paymentRepository
                .findByTransactionId(
                        transactionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transaction not found"));

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS);

        return paymentRepository.save(
                payment);
    }

    // =========================
    // RECEPTION CASH PAYMENT
    // =========================

    public Payment receiveCashPayment(
            Long bookingId,
            CashPaymentRequest request) {

        Booking booking = bookingRepository.findById(
                bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cannot process cash payment for a cancelled booking");
        }

        if (booking.getRoom().getStatus() != RoomStatus.BOOKED) {
            throw new RuntimeException(
                    "Room is not currently booked for this payment");
        }

        Payment payment = paymentRepository
                .findByBookingId(bookingId)
                .orElse(new Payment());

        payment.setBooking(booking);

        double amount = request.getAmount() != null
                && request.getAmount() > 0
                ? request.getAmount()
                : booking.getTotalPrice();

        payment.setAmount(amount);
        payment.setPaymentMethod(
                PaymentMethod.CASH);
        payment.setPaymentStatus(
                PaymentStatus.SUCCESS);

        String transactionId = request.getTransactionId();

        if (transactionId == null || transactionId.isBlank()) {
            transactionId = "CASH-" + bookingId + "-" +
                    System.currentTimeMillis();
        }

        payment.setTransactionId(transactionId);

        return paymentRepository.save(payment);
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================

    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================

    public Payment getPaymentById(
            Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found"));
    }

    // =========================
    // DELETE PAYMENT
    // =========================

    public void deletePayment(
            Long id) {

        Payment payment =
                getPaymentById(id);

        paymentRepository.delete(payment);
    }

    // =========================
    // GET PAYMENTS BY STATUS
    // =========================

    public List<Payment> getPaymentsByStatus(
            PaymentStatus paymentStatus) {

        return paymentRepository
                .findByPaymentStatus(
                        paymentStatus);
    }

    // =========================
    // REFUND PAYMENT
    // =========================

    public Payment refundPayment(
            Long paymentId) {

        Payment payment =
                getPaymentById(paymentId);

        payment.setPaymentStatus(
                PaymentStatus.REFUNDED);

        return paymentRepository.save(
                payment);
    }
}