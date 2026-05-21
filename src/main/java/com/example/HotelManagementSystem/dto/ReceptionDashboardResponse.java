package com.example.HotelManagementSystem.dto;

import java.util.List;

import com.example.HotelManagementSystem.entity.Rooms;

public class ReceptionDashboardResponse {

    private List<Rooms> availableRooms;
    private List<String> suggestions;

    public ReceptionDashboardResponse() {
    }

    public ReceptionDashboardResponse(
            List<Rooms> availableRooms,
            List<String> suggestions) {

        this.availableRooms = availableRooms;
        this.suggestions = suggestions;
    }

    public List<Rooms> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<Rooms> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}