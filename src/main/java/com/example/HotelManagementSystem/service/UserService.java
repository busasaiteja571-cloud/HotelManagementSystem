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
    // OTP Service
    // =========================

    private final OTPService otpService;

    // =========================
    // BCrypt Password Encoder
    // =========================

    private final BCryptPasswordEncoder passwordEncoder;

    // =========================
    // Constructor Injection
    // =========================

    public UserService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            OTPService otpService) {

        this.userRepository = userRepository;

        this.jwtUtil = jwtUtil;

        this.otpService = otpService;

        this.passwordEncoder =
                new BCryptPasswordEncoder();
    }

    // =========================
    // CREATE USER
    // =========================

    public User createUser(UserRegistrationRequest request) {
        return saveNewUser(
                toUser(request),
                Role.CUSTOMER
        );
    }

    public User createStaff(UserCreateRequest request) {
        return saveNewUser(
                toUser(request),
                Role.STAFF
        );
    }

    public User createAdmin(UserCreateRequest request) {
        return saveNewUser(
                toUser(request),
                Role.ADMIN
        );
    }

    public User createUser(User user) {
        return saveNewUser(user, Role.CUSTOMER);
    }

    public User createStaff(User user) {
        return saveNewUser(user, Role.STAFF);
    }

    public User createAdmin(User user) {
        return saveNewUser(user, Role.ADMIN);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    private User toUser(UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        return user;
    }

    private User toUser(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        return user;
    }

    private User saveNewUser(User user, Role role) {

        // Check Username Already Exists
        userRepository.findByUsername(
                user.getUsername())
                .ifPresent(existingUser -> {

                    throw new RuntimeException(
                            "Username already exists");
                });

        // Check Email Already Exists
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

        // Force role assignment based on the operation
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
    // GET USER BY ID
    // =========================

    public User getUserById(Long id) {

        return userRepository.findById(id)
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

        // Update Password Only If Provided
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(

                    passwordEncoder.encode(
                            user.getPassword())
            );
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

        // Compare Encrypted Password
        boolean isPasswordMatch =

                passwordEncoder.matches(
                        password,
                        user.getPassword());

        if (!isPasswordMatch) {

            throw new RuntimeException(
                    "Invalid password");
        }

        // Generate JWT Token
        return jwtUtil.generateToken(
                user.getUsername());
    }

    // =========================
    // VERIFY EMAIL WITH OTP
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
                userRepository.findByEmail(email)
                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"));

        user.setIsEmailVerified(true);
        userRepository.save(user);
    }

    // =========================
    // GET USER BY EMAIL
    // =========================

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"));
    }
}