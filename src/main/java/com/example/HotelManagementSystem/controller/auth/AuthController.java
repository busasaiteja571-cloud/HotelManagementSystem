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

import com.example.HotelManagementSystem.dto.UserRegistrationRequest;
import com.example.HotelManagementSystem.dto.UserResponse;
import com.example.HotelManagementSystem.entity.LoginRequest;
import com.example.HotelManagementSystem.entity.OTPRequest;
import com.example.HotelManagementSystem.entity.OTPVerificationRequest;
import com.example.HotelManagementSystem.service.OTPService;
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

    private final OTPService otpService;

    public AuthController(
            UserService userService,
            OTPService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    // =========================
    // REGISTER USER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        UserResponse response = UserResponse.fromUser(
                userService.createUser(request)
        );

        return new ResponseEntity<>(
                response,
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

    // =========================
    // SEND OTP
    // =========================

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOTP(
            @Valid @RequestBody OTPRequest request) {

        otpService.generateAndSendOTP(
                request.getEmail());

        Map<String, Object> response =
                new HashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status", 200);

        response.put("message",
                "OTP sent successfully to your email");

        return ResponseEntity.ok(response);
    }

    // =========================
    // VERIFY OTP
    // =========================

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOTP(
            @Valid @RequestBody OTPVerificationRequest request) {

        userService.verifyEmailWithOTP(
                request.getEmail(),
                request.getCode());

        Map<String, Object> response =
                new HashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status", 200);

        response.put("message",
                "Email verified successfully");

        return ResponseEntity.ok(response);
    }
}