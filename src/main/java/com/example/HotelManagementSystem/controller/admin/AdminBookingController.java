package com.example.HotelManagementSystem.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.service.BookingService;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    // =========================
    // Service Injection
    // =========================

    private final BookingService bookingService;

    public AdminBookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    // =========================
    // GET ALL BOOKINGS
    // =========================

    @GetMapping
    public List<Booking> getAllBookings() {

        return bookingService.getAllBookings();
    }

    // =========================
    // GET BOOKING BY ID
    // =========================

    @GetMapping("/{id}")
    public Booking getBookingById(
            @PathVariable Long id) {

        return bookingService.getBookingById(id);
    }

    // =========================
    // DELETE BOOKING
    // =========================

    @DeleteMapping("/{id}")
    public void deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);
    }
}