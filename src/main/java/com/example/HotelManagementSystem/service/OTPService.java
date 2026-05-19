package com.example.HotelManagementSystem.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.entity.OTP;
import com.example.HotelManagementSystem.repository.OTPRepository;

@Service
public class OTPService {

    // =========================
    // Repository Injection
    // =========================

    private final OTPRepository otpRepository;

    private final EmailService emailService;

    // =========================
    // Constants
    // =========================

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;

    // =========================
    // Constructor Injection
    // =========================

    public OTPService(
            OTPRepository otpRepository,
            EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    // =========================
    // GENERATE AND SEND OTP
    // =========================

    public void generateAndSendOTP(String email) {

        try {

            // Invalidate previous OTPs for this email
            Optional<OTP> existingOTP =
                    otpRepository.findByEmailAndIsUsedFalse(email);

            if (existingOTP.isPresent()) {
                OTP otp = existingOTP.get();
                otp.setIsUsed(true);
                otpRepository.save(otp);
            }

            // Generate OTP Code
            String otpCode = generateOTPCode();

            // Calculate Expiry Time
            LocalDateTime expiryTime =
                    LocalDateTime.now()
                    .plusMinutes(OTP_EXPIRY_MINUTES);

            // Create OTP Entity
            OTP otp = new OTP(
                    email,
                    otpCode,
                    expiryTime);

            // Save OTP
            otpRepository.save(otp);

            // Send OTP via Email
            String subject =
                    "Hotel Management System - OTP Verification";

            String body = buildOTPEmailBody(otpCode);

            emailService.sendEmail(
                    email,
                    subject,
                    body);

            System.out.println(
                    "OTP sent successfully to: " + email);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate and send OTP: " +
                    e.getMessage());
        }
    }

    // =========================
    // VERIFY OTP
    // =========================

    public boolean verifyOTP(
            String email,
            String code) {

        Optional<OTP> otpOptional =
                otpRepository
                .findByEmailAndCodeAndIsUsedFalse(
                        email,
                        code);

        if (otpOptional.isEmpty()) {

            return false;
        }

        OTP otp = otpOptional.get();

        // Check if OTP is expired
        if (LocalDateTime.now()
                .isAfter(otp.getExpiryTime())) {

            return false;
        }

        // Mark OTP as used
        otp.setIsUsed(true);
        otpRepository.save(otp);

        return true;
    }

    // =========================
    // GENERATE OTP CODE
    // =========================

    private String generateOTPCode() {

        Random random = new Random();

        int otpCode = 100000 +
                random.nextInt(900000);

        return String.valueOf(otpCode);
    }

    // =========================
    // BUILD OTP EMAIL BODY
    // =========================

    private String buildOTPEmailBody(String otpCode) {

        return "Dear User,\n\n" +
                "Your OTP (One-Time Password) for Hotel Management System is:\n\n" +
                "OTP: " + otpCode + "\n\n" +
                "This OTP is valid for 5 minutes only.\n\n" +
                "Please do not share this OTP with anyone.\n\n" +
                "If you did not request this OTP, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Hotel Management System Team";
    }
}
