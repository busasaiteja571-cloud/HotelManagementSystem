package com.example.HotelManagementSystem.controller.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.LoginRequest;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    // =========================
    // Service Injection
    // =========================

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // REGISTER USER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @Valid @RequestBody User user) {

        User savedUser = userService.createUser(user);

        return new ResponseEntity<>(
                savedUser,
                HttpStatus.CREATED);
    }

    // =========================
    // LOGIN USER
    // =========================

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(
            @RequestBody LoginRequest request) {

        String token = userService.loginUser(
                request.getUsername(),
                request.getPassword());

        Map<String, Object> response =
                new HashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status", 200);

        response.put("message",
                "Login successful");

        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}