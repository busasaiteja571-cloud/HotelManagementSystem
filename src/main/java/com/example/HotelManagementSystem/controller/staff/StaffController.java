package com.example.HotelManagementSystem.controller.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.BookingStatus;
import com.example.HotelManagementSystem.entity.Payment;
import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.service.BookingService;
import com.example.HotelManagementSystem.service.PaymentService;
import com.example.HotelManagementSystem.service.RoomService;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:4200")
public class StaffController {

    private final BookingService bookingService;

    private final RoomService roomService;

    private final PaymentService paymentService;

    public StaffController(
            BookingService bookingService,
            RoomService roomService,
            PaymentService paymentService) {

        this.bookingService = bookingService;
        this.roomService = roomService;
        this.paymentService = paymentService;
    }

    // =========================
    // BOOKING ENDPOINTS
    // =========================

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {

        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/{id}")
    public Booking getBookingById(
            @PathVariable Long id) {

        return bookingService.getBookingById(id);
    }

    @GetMapping("/bookings/user/{userId}")
    public List<Booking> getBookingsByUser(
            @PathVariable Long userId) {

        return bookingService.getBookingsByUser(userId);
    }

    @GetMapping("/bookings/room/{roomId}")
    public List<Booking> getBookingsByRoom(
            @PathVariable Long roomId) {

        return bookingService.getBookingsByRoom(roomId);
    }

    @PutMapping("/bookings/{id}/status")
    public Booking updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        String statusValue = payload.get("bookingStatus");

        BookingStatus bookingStatus =
                BookingStatus.valueOf(statusValue);

        return bookingService.updateBookingStatus(
                id,
                bookingStatus);
    }

    // =========================
    // ROOM ENDPOINTS
    // =========================

    @GetMapping("/rooms")
    public List<Rooms> getAllRooms() {

        return roomService.getAllRooms();
    }

    @PutMapping("/rooms/{id}/status")
    public Rooms updateRoomStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        String statusValue = payload.get("status");

        RoomStatus status = RoomStatus.valueOf(statusValue);

        return roomService.updateRoomStatus(
                id,
                status);
    }

    // =========================
    // PAYMENT ENDPOINTS
    // =========================

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {

        return paymentService.getAllPayments();
    }

    @PutMapping("/payments/refund/{paymentId}")
    public Payment refundPayment(
            @PathVariable Long paymentId) {

        return paymentService.refundPayment(paymentId);
    }
}
