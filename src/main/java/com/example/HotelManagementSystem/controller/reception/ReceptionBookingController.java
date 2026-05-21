package com.example.HotelManagementSystem.controller.reception;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.dto.ReceptionBookingRequest;
import com.example.HotelManagementSystem.dto.ReceptionBookingResponse;
import com.example.HotelManagementSystem.dto.ReceptionDashboardResponse;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.service.BookingService;
import com.example.HotelManagementSystem.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reception")
@Validated
public class ReceptionBookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    public ReceptionBookingController(
            BookingService bookingService,
            RoomService roomService) {

        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @PostMapping("/bookings")
    public ReceptionBookingResponse createReceptionBooking(
            @Valid @RequestBody ReceptionBookingRequest request) {

        return bookingService.createReceptionBooking(
                request);
    }

    @GetMapping("/dashboard")
    public ReceptionDashboardResponse getReceptionDashboard() {

        List<Rooms> availableRooms =
                roomService.getAvailableRooms();

        List<String> suggestions = List.of(
                "Use POST /api/reception/bookings to create a new booking.",
                "After booking, use PUT /api/reception/payments/cash/{bookingId} to record cash payment.",
                "Confirm guest check-in and update room status as needed.",
                "Verify customer data and share login credentials if you created a new account.");

        return new ReceptionDashboardResponse(
                availableRooms,
                suggestions);
    }
}