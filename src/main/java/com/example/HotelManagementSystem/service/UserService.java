package com.example.HotelManagementSystem.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.dto.UserCreateRequest;
import com.example.HotelManagementSystem.dto.UserRegistrationRequest;
import com.example.HotelManagementSystem.entity.Role;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.repository.UserRepository;
import com.example.HotelManagementSystem.security.JwtUtil;

@Service
public class UserService {

    // =========================
    // Repository Injection
    // =========================

    private final UserRepository userRepository;

    // =========================
    // JWT UTIL
    // =========================

    private final JwtUtil jwtUtil;

    // =========================
    // OTP SERVICE
    // =========================

    private final OTPService otpService;

    // =========================
    // PASSWORD ENCODER
    // =========================

    private final BCryptPasswordEncoder passwordEncoder;

    // =========================
    // CONSTRUCTOR INJECTION
    // =========================

    public UserService(

            UserRepository userRepository,

            JwtUtil jwtUtil,

            OTPService otpService

    ) {

        this.userRepository = userRepository;

        this.jwtUtil = jwtUtil;

        this.otpService = otpService;

        this.passwordEncoder =
                new BCryptPasswordEncoder();
    }

    // =========================
    // CREATE CUSTOMER
    // =========================

    public User createUser(
            UserRegistrationRequest request) {

        return saveNewUser(

                toUser(request),

                Role.CUSTOMER
        );
    }

    // =========================
    // CREATE STAFF
    // =========================

    public User createStaff(
            UserCreateRequest request) {

        return saveNewUser(

                toUser(request),

                Role.STAFF
        );
    }

    // =========================
    // CREATE ADMIN
    // =========================

    public User createAdmin(
            UserCreateRequest request) {

        return saveNewUser(

                toUser(request),

                Role.ADMIN
        );
    }

    // =========================
    // CONVERT DTO TO USER
    // =========================

    private User toUser(
            UserRegistrationRequest request) {

        User user = new User();

        user.setUsername(
                request.getUsername());

        user.setEmail(
                request.getEmail());

        user.setPassword(
                request.getPassword());

        user.setPhone(
                request.getPhone());

        return user;
    }

    private User toUser(
            UserCreateRequest request) {

        User user = new User();

        user.setUsername(
                request.getUsername());

        user.setEmail(
                request.getEmail());

        user.setPassword(
                request.getPassword());

        user.setPhone(
                request.getPhone());

        return user;
    }

    // =========================
    // SAVE USER
    // =========================

    private User saveNewUser(
            User user,
            Role role) {

        // Username Exists
        userRepository.findByUsername(
                user.getUsername())

                .ifPresent(existingUser -> {

                    throw new RuntimeException(
                            "Username already exists");
                });

        // Email Exists
        userRepository.findByEmail(
                user.getEmail())

                .ifPresent(existingUser -> {

                    throw new RuntimeException(
                            "Email already exists");
                });

        // Encrypt Password
        user.setPassword(

                passwordEncoder.encode(
                        user.getPassword())
        );

        // Set Role
        user.setRole(role);

        return userRepository.save(user);
    }

    // =========================
    // GET ALL USERS
    // =========================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // =========================
    // GET USER BY ROLE
    // =========================

    public List<User> getUsersByRole(
            Role role) {

        return userRepository.findByRole(role);
    }

    // =========================
    // GET USER BY ID
    // =========================

    public User getUserById(Long id) {

        return userRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"));
    }

    // =========================
    // GET USER BY EMAIL
    // =========================

    public User getUserByEmail(
            String email) {

        return userRepository.findByEmail(email)

                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"));
    }

    // =========================
    // UPDATE USER
    // =========================

    public User updateUser(
            Long id,
            User user) {

        User existingUser =
                getUserById(id);

        existingUser.setUsername(
                user.getUsername());

        existingUser.setEmail(
                user.getEmail());

        existingUser.setPhone(
                user.getPhone());

        existingUser.setRole(
                user.getRole());

        // Update password if provided
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(

                    passwordEncoder.encode(
                            user.getPassword()));
        }

        return userRepository.save(
                existingUser);
    }

    // =========================
    // UPDATE STAFF
    // =========================

    public User updateStaff(

            Long id,

            UserCreateRequest request) {

        User existingUser =
                getUserById(id);

        // Ensure user is STAFF
        if (existingUser.getRole() != Role.STAFF) {

            throw new RuntimeException(
                    "User is not staff");
        }

        existingUser.setUsername(
                request.getUsername());

        existingUser.setEmail(
                request.getEmail());

        existingUser.setPhone(
                request.getPhone());

        // Update password if provided
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            existingUser.setPassword(

                    passwordEncoder.encode(
                            request.getPassword()));
        }

        return userRepository.save(
                existingUser);
    }

    // =========================
    // DELETE USER
    // =========================

    public void deleteUser(Long id) {

        User user =
                getUserById(id);

        userRepository.delete(user);
    }

    // =========================
    // LOGIN USER
    // =========================

    public String loginUser(

            String username,

            String password) {

        User user =

                userRepository.findByUsername(
                        username)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Invalid username"));

        // Match Password
        boolean isPasswordMatch =

                passwordEncoder.matches(

                        password,

                        user.getPassword());

        if (!isPasswordMatch) {

            throw new RuntimeException(
                    "Invalid password");
        }

        // Generate JWT
        return jwtUtil.generateToken(
                user.getUsername());
    }

    // =========================
    // VERIFY EMAIL OTP
    // =========================

    public void verifyEmailWithOTP(

            String email,

            String otpCode) {

        boolean isOTPValid =

                otpService.verifyOTP(
                        email,
                        otpCode);

        if (!isOTPValid) {

            throw new RuntimeException(
                    "Invalid or expired OTP");
        }

        User user =

                userRepository.findByEmail(
                        email)

                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"));

        user.setIsEmailVerified(true);

        userRepository.save(user);
    }
}