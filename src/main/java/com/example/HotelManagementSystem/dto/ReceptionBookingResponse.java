package com.example.HotelManagementSystem.dto;

public class ReceptionBookingResponse {

    private Long bookingId;
    private String username;
    private String password;
    private String message;

    public ReceptionBookingResponse() {
    }

    public ReceptionBookingResponse(
            Long bookingId,
            String username,
            String password,
            String message) {

        this.bookingId = bookingId;
        this.username = username;
        this.password = password;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}