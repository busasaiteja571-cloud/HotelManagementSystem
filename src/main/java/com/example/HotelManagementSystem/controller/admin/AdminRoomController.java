package com.example.HotelManagementSystem.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.RoomType;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.service.RoomService;

@RestController
@RequestMapping("/api/admin/rooms")
@CrossOrigin("*")
public class AdminRoomController {

    // =========================
    // Service Injection
    // =========================

    private final RoomService roomService;

    public AdminRoomController(RoomService roomService) {

        this.roomService = roomService;
    }

    // =========================
    // CREATE ROOM
    // =========================

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Rooms createRoom(

            @RequestParam String roomNumber,

            @RequestParam RoomType roomType,

            @RequestParam Double price,

            @RequestParam Integer capacity,

            @RequestParam RoomStatus status,

            @RequestParam MultipartFile image,

            @RequestParam String description

    ) throws IOException {

        Rooms room = new Rooms();

        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setPrice(price);
        room.setCapacity(capacity);
        room.setStatus(status);
        room.setDescription(description);

        // Save uploaded image
        String imagePath =
                roomService.saveUploadedFile(image);

        room.setImageUrl(imagePath);

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

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Rooms updateRoom(

            @PathVariable Long id,

            @RequestParam String roomNumber,

            @RequestParam RoomType roomType,

            @RequestParam Double price,

            @RequestParam Integer capacity,

            @RequestParam RoomStatus status,

            @RequestParam(required = false)
            MultipartFile image,

            @RequestParam String description

    ) throws IOException {

        Rooms room = new Rooms();

        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setPrice(price);
        room.setCapacity(capacity);
        room.setStatus(status);
        room.setDescription(description);

        // upload image if provided
        if (image != null && !image.isEmpty()) {

            String imagePath =
                    roomService.saveUploadedFile(image);

            room.setImageUrl(imagePath);
        }

        return roomService.updateRoom(id, room);
    }

    // =========================
    // DELETE ROOM
    // =========================

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {

        roomService.deleteRoom(id);
    }
}