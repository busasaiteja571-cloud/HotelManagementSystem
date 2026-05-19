package com.example.HotelManagementSystem.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    // =========================
    // Service Injection
    // =========================

    private final UserService userService;

    public AdminUserController(
            UserService userService) {

        this.userService = userService;
    }

    // =========================
    // GET ALL USERS
    // =========================

    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    // =========================
    // GET USER BY ID
    // =========================

    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // =========================
    // DELETE USER
    // =========================

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);
    }
}