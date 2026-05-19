package com.example.HotelManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.Rooms;

public interface RoomRepository extends JpaRepository<Rooms, Long> {

    Optional<Rooms> findByRoomNumber(String roomNumber);

    List<Rooms> findByStatus(RoomStatus status);

    List<Rooms> findByRoomType(String roomType);
}