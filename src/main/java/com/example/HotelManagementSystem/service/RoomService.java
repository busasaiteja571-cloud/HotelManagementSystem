package com.example.HotelManagementSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.repository.RoomRepository;

@Service
public class RoomService {

	  // Repository Injection

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // CREATE ROOM
    
    
    public Rooms createRoom(Rooms room) {

        // Check Duplicate Room Number
        roomRepository.findByRoomNumber(room.getRoomNumber())
                .ifPresent(existingRoom -> {
                    throw new RuntimeException("Room number already exists");
                });

        // Price Validation
        if (room.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }

        // Capacity Validation
        if (room.getCapacity() <= 0) {
            throw new RuntimeException("Capacity must be greater than 0");
        }

        return roomRepository.save(room);
    }

    // GET ALL ROOMS

    public List<Rooms> getAllRooms() {

        return roomRepository.findAll();
    }

    // GET ROOM BY ID

    public Rooms getRoomById(Long id) {

        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));
    }

    // UPDATE ROOM

    public Rooms updateRoom(Long id, Rooms room) {

        Rooms existingRoom = getRoomById(id);

        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setRoomType(room.getRoomType());
        existingRoom.setPrice(room.getPrice());
        existingRoom.setCapacity(room.getCapacity());
        existingRoom.setStatus(room.getStatus());
        existingRoom.setImageUrl(room.getImageUrl());
        existingRoom.setDescription(room.getDescription());

        return roomRepository.save(existingRoom);
    }

    // DELETE ROOM

    public void deleteRoom(Long id) {

        Rooms room = getRoomById(id);

        roomRepository.delete(room);
    }

    // GET AVAILABLE ROOMS

    public List<Rooms> getAvailableRooms() {

        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }
}