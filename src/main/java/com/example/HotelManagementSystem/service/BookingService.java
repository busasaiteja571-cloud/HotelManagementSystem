package com.example.HotelManagementSystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.dto.ReceptionBookingRequest;
import com.example.HotelManagementSystem.dto.ReceptionBookingResponse;
import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.BookingStatus;
import com.example.HotelManagementSystem.entity.RoomStatus;
import com.example.HotelManagementSystem.entity.Rooms;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.repository.BookingRepository;
import com.example.HotelManagementSystem.repository.RoomRepository;
import com.example.HotelManagementSystem.repository.UserRepository;

@Service
public class BookingService {

    // =========================
    // Repository Injection
    // =========================

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            UserService userService) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    // CREATE BOOKING
   

    public Booking createBooking(Booking booking) {

        // Check User Exists
        userRepository.findById(
                booking.getUser().getId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Check Room Exists
        Rooms room = roomRepository.findById(
                booking.getRoom().getId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        // Validate Dates
        if (booking.getCheckOut()
                .isBefore(booking.getCheckIn())) {

            throw new RuntimeException(
                    "Check-out date cannot be before check-in date");
        }

        // Check Room Availability
        boolean isBooked =
                bookingRepository
                .existsByRoomAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                        room,
                        booking.getCheckOut(),
                        booking.getCheckIn());

        if (isBooked) {
            throw new RuntimeException("Room is already booked");
        }

        // Calculate Total Price
        long days =
                booking.getCheckIn()
                .until(booking.getCheckOut())
                .getDays();

        double totalPrice =
                days * room.getPrice();

        booking.setTotalPrice(totalPrice);

        // Set Booking Status
        booking.setBookingStatus(
                BookingStatus.CONFIRMED);

        // Update Room Status
        room.setStatus(RoomStatus.BOOKED);

        roomRepository.save(room);

        // Save Booking
        return bookingRepository.save(booking);
    }

    // =========================
    // CREATE BOOKING FROM RECEPTION
    // =========================

    public ReceptionBookingResponse createReceptionBooking(
            ReceptionBookingRequest request) {

        User user = userRepository
                .findByUsername(
                        request.getUser().getUsername())
                .orElseGet(() ->
                        userRepository
                                .findByEmail(
                                        request.getUser().getEmail())
                                .orElse(null));

        boolean accountCreated = false;
        String rawPassword = null;

        if (user == null) {
            user = userService.createCustomer(
                    request.getUser());
            accountCreated = true;
            rawPassword = request.getUser().getPassword();
        }

        Rooms room = roomRepository.findById(
                request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        if (request.getCheckOut()
                .isBefore(request.getCheckIn())) {

            throw new RuntimeException(
                    "Check-out date cannot be before check-in date");
        }

        boolean isBooked = bookingRepository
                .existsByRoomAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                        room,
                        request.getCheckOut(),
                        request.getCheckIn());

        if (isBooked) {
            throw new RuntimeException("Room is already booked");
        }

        long days = request.getCheckIn()
                .until(request.getCheckOut())
                .getDays();

        double totalPrice = days * room.getPrice();

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setTotalPrice(totalPrice);
        booking.setDiscountAmount(0.0);
        booking.setFinalPrice(totalPrice);
        booking.setBookingStatus(
                BookingStatus.CONFIRMED);

        room.setStatus(RoomStatus.BOOKED);

        roomRepository.save(room);

        Booking savedBooking = bookingRepository.save(booking);

        String message = accountCreated
                ? "Reception booking created and customer account created."
                : "Reception booking created for existing user.";

        return new ReceptionBookingResponse(
                savedBooking.getId(),
                user.getUsername(),
                rawPassword,
                message);
    }

 
    // GET ALL BOOKINGS
 

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }


    // GET BOOKING BY ID
  

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }


    // UPDATE BOOKING


    public Booking updateBooking(Long id, Booking booking) {

        Booking existingBooking = getBookingById(id);

        existingBooking.setCheckIn(
                booking.getCheckIn());

        existingBooking.setCheckOut(
                booking.getCheckOut());

        existingBooking.setBookingStatus(
                booking.getBookingStatus());

        return bookingRepository.save(existingBooking);
    }
    //Booking status 
    public Booking updateBookingStatus(
            Long id,
            BookingStatus bookingStatus) {

        Booking booking = getBookingById(id);

        booking.setBookingStatus(bookingStatus);

        return bookingRepository.save(booking);
    }


    // DELETE BOOKING


    public void deleteBooking(Long id) {

        Booking booking = getBookingById(id);

        bookingRepository.delete(booking);
    }


    // GET BOOKINGS BY USER


    public List<Booking> getBookingsByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return bookingRepository.findByUser(user);
    }

  
    // GET BOOKINGS BY ROOM


    public List<Booking> getBookingsByRoom(Long roomId) {

        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        return bookingRepository.findByRoom(room);
    }


    // CANCEL BOOKING
   

    public Booking cancelBooking(Long bookingId) {

        Booking booking = getBookingById(bookingId);

        booking.setBookingStatus(
                BookingStatus.CANCELLED);

        Rooms room = booking.getRoom();

        room.setStatus(RoomStatus.AVAILABLE);

        roomRepository.save(room);

        return bookingRepository.save(booking);
    }


    // CHECK ROOM AVAILABILITY
   

    public boolean isRoomAvailable(
            Rooms room,
            LocalDate checkIn,
            LocalDate checkOut) {

        return !bookingRepository
                .existsByRoomAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                        room,
                        checkOut,
                        checkIn);
    }


    // CALCULATE TOTAL PRICE


    public Double calculateTotalPrice(
            Rooms room,
            LocalDate checkIn,
            LocalDate checkOut) {

        long days =
                checkIn.until(checkOut).getDays();

        return days * room.getPrice();
    }
}