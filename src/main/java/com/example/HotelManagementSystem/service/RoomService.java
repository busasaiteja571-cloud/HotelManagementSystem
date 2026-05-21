package com.example.HotelManagementSystem.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.repository.RoomRepository;

@Service
public class RoomService {

    // =========================
    // Repository Injection
    // =========================

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {

        this.roomRepository = roomRepository;
    }

    // =========================
    // CREATE ROOM
    // =========================

    public Rooms createRoom(Rooms room) {

        // Check Duplicate Room Number
        roomRepository.findByRoomNumber(
                room.getRoomNumber())

                .ifPresent(existingRoom -> {

                    throw new RuntimeException(
                            "Room number already exists");
                });

        // Price Validation
        if (room.getPrice() <= 0) {

            throw new RuntimeException(
                    "Price must be greater than 0");
        }

        // Capacity Validation
        if (room.getCapacity() <= 0) {

            throw new RuntimeException(
                    "Capacity must be greater than 0");
        }

        return roomRepository.save(room);
    }

    // =========================
    // GET ALL ROOMS
    // =========================

    public List<Rooms> getAllRooms() {

        return roomRepository.findAll();
    }

    // =========================
    // GET ROOM BY ID
    // =========================

    public Rooms getRoomById(Long id) {

        return roomRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Room not found"));
    }

    // =========================
    // UPDATE ROOM
    // =========================

    public Rooms updateRoom(
            Long id,
            Rooms room) {

        Rooms existingRoom =
                getRoomById(id);

        existingRoom.setRoomNumber(
                room.getRoomNumber());

        existingRoom.setRoomType(
                room.getRoomType());

        existingRoom.setPrice(
                room.getPrice());

        existingRoom.setCapacity(
                room.getCapacity());

        existingRoom.setStatus(
                room.getStatus());

        existingRoom.setDescription(
                room.getDescription());

        // Update image only if new image exists
        if (room.getImageUrl() != null) {

            existingRoom.setImageUrl(
                    room.getImageUrl());
        }

        return roomRepository.save(
                existingRoom);
    }

    public Rooms updateRoomStatus(
            Long id,
            RoomStatus status) {

        Rooms existingRoom =
                getRoomById(id);

        existingRoom.setStatus(status);

        return roomRepository.save(
                existingRoom);
    }

    // =========================
    // DELETE ROOM
    // =========================

    public void deleteRoom(Long id) {

        Rooms room =
                getRoomById(id);

        roomRepository.delete(room);
    }

    // =========================
    // GET AVAILABLE ROOMS
    // =========================

    public List<Rooms> getAvailableRooms() {

        return roomRepository.findByStatus(
                RoomStatus.AVAILABLE);
    }

    // =========================
    // SAVE IMAGE FILE
    // =========================

    public String saveUploadedFile(
            MultipartFile file)
            throws IOException {

        // Validation
        if (file == null ||
                file.isEmpty()) {

            return null;
        }

        // Get Original Filename
        String originalFileName =
                file.getOriginalFilename();

        // File Extension
        String extension = ".jpg";

        if (originalFileName != null &&
                originalFileName.contains(".")) {

            extension =
                    originalFileName.substring(

                            originalFileName
                                    .lastIndexOf("."));
        }

        // Generate Unique File Name
        String fileName =
                UUID.randomUUID()
                        + extension;

        // Upload Folder Path
        Path uploadPath =
                Paths.get(
                        "src/main/resources/static/images");

        // Create Folder If Not Exists
        if (!Files.exists(uploadPath)) {

            Files.createDirectories(
                    uploadPath);
        }

        // Final File Path
        Path filePath =
                uploadPath.resolve(fileName);

        // Save File
        try (InputStream inputStream =
                     file.getInputStream()) {

            Files.copy(
                    inputStream,
                    filePath);
        }

        // Return Image URL
        return "/images/" + fileName;
    }
}