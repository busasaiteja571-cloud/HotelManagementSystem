package com.example.HotelManagementSystem.controller.admin;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.dto.UserCreateRequest;
import com.example.HotelManagementSystem.dto.UserResponse;
import com.example.HotelManagementSystem.entity.Role;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/admin/staff")
public class AdminStaffController {

    private final UserService userService;

    public AdminStaffController(
            UserService userService) {

        this.userService = userService;
    }

    // =========================
    // GET ALL STAFF
    // =========================

    @GetMapping
    public List<UserResponse> getAllStaff() {

        return userService.getUsersByRole(
                        Role.STAFF)

                .stream()

                .map(UserResponse::fromUser)

                .toList();
    }

    // =========================
    // GET STAFF BY ID
    // =========================

    @GetMapping("/{id}")
    public UserResponse getStaffById(
            @PathVariable Long id) {

        User user =
                userService.getUserById(id);

        if (user.getRole() != Role.STAFF) {

            throw new RuntimeException(
                    "Staff member not found");
        }

        return UserResponse.fromUser(user);
    }

    // =========================
    // CREATE STAFF
    // =========================

    @PostMapping
    public UserResponse createStaff(

            @Valid
            @RequestBody
            UserCreateRequest request) {

        return UserResponse.fromUser(

                userService.createStaff(request)
        );
    }

    // =========================
    // UPDATE STAFF
    // =========================

    @PutMapping("/{id}")
    public UserResponse updateStaff(

            @PathVariable Long id,

            @Valid
            @RequestBody
            UserCreateRequest request) {

        User updatedStaff =

                userService.updateStaff(
                        id,
                        request);

        return UserResponse.fromUser(
                updatedStaff);
    }

    // =========================
    // DELETE STAFF
    // =========================

    @DeleteMapping("/{id}")
    public void deleteStaff(
            @PathVariable Long id) {

        User staff =
                userService.getUserById(id);

        if (staff.getRole() != Role.STAFF) {

            throw new RuntimeException(
                    "User is not staff");
        }

        userService.deleteUser(id);
    }
}