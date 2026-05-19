package com.example.HotelManagementSystem.controller.auth;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // =========================
    // Service Injection
    // =========================

    private final UserService userService;

    public AuthController(
            UserService userService) {

        this.userService = userService;
    }

    // =========================
    // REGISTER USER
    // =========================

    public User registerUser(
            @RequestBody User user) {

        return userService.createUser(user);
    }

    // =========================
    // LOGIN USER
    // =========================

    public User loginUser(
            String username,
            String password) {

        return userService.loginUser(
                username,
                password);
    }
}