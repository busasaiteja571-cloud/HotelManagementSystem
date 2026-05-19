package com.example.HotelManagementSystem.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.service.BookingService;

@RestController
@RequestMapping("/api/user/bookings")
public class UserBookingController {

    // =========================
    // Service Injection
    // =========================

    private final BookingService bookingService;

    public UserBookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @PostMapping
    public Booking createBooking(
            @RequestBody Booking booking) {

        return bookingService.createBooking(
                booking);
    }

    // =========================
    // GET USER BOOKINGS
    // =========================

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(
            @PathVariable Long userId) {

        return bookingService.getBookingsByUser(
                userId);
    }

    // =========================
    // CANCEL BOOKING
    // =========================

    @PutMapping("/cancel/{bookingId}")
    public Booking cancelBooking(
            @PathVariable Long bookingId) {

        return bookingService.cancelBooking(
                bookingId);
    }
}