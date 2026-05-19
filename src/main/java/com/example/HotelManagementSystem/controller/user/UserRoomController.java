package com.example.HotelManagementSystem.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.service.RoomService;

@RestController
@RequestMapping("/api/user/rooms")
public class UserRoomController {

    // =========================
    // Service Injection
    // =========================

    private final RoomService roomService;

    public UserRoomController(
            RoomService roomService) {

        this.roomService = roomService;
    }

    // =========================
    // GET ALL ROOMS
    // =========================

    @GetMapping
    public List<Rooms> getAllRooms() {

        return roomService.getAllRooms();
    }

    // =========================
    // GET AVAILABLE ROOMS
    // =========================

    @GetMapping("/available")
    public List<Rooms> getAvailableRooms() {

        return roomService.getAvailableRooms();
    }
}