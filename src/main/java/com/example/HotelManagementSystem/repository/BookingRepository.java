package com.example.HotelManagementSystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.BookingStatus;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // GET BOOKINGS BY USER

    List<Booking> findByUser(User user);

    // GET BOOKINGS BY ROOM

    List<Booking> findByRoom(Rooms room);

    // GET BOOKINGS BY STATUS

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    // GET BOOKINGS BETWEEN DATES

    List<Booking> findByCheckInBetween(
            LocalDate startDate,
            LocalDate endDate);


    // CHECK ROOM BOOKED


    boolean existsByRoomAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            Rooms room,
            LocalDate checkOut,
            LocalDate checkIn);


    // GET BOOKINGS BY USER ID


    List<Booking> findByUserId(Long userId);


    // GET BOOKINGS BY ROOM ID


    List<Booking> findByRoomId(Long roomId);
}