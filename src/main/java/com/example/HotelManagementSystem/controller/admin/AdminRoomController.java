package com.example.HotelManagementSystem.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.service.RoomService;

@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    // =========================
    // Service Injection
    // =========================

    private final RoomService roomService;

    public AdminRoomController(
            RoomService roomService) {

        this.roomService = roomService;
    }

    // =========================
    // CREATE ROOM
    // =========================

    @PostMapping
    public Rooms createRoom(
            @RequestBody Rooms room) {

        return roomService.createRoom(room);
    }

    // =========================
    // GET ALL ROOMS
    // =========================

    @GetMapping
    public List<Rooms> getAllRooms() {

        return roomService.getAllRooms();
    }

    // =========================
    // UPDATE ROOM
    // =========================

    @PutMapping("/{id}")
    public Rooms updateRoom(
            @PathVariable Long id,
            @RequestBody Rooms room) {

        return roomService.updateRoom(
                id,
                room);
    }

    // =========================
    // DELETE ROOM
    // =========================

    @DeleteMapping("/{id}")
    public void deleteRoom(
            @PathVariable Long id) {

        roomService.deleteRoom(id);
    }
}