package com.example.HotelManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByEmailAndCodeAndIsUsedFalse(String email, String code);

    Optional<OTP> findByEmailAndIsUsedFalse(String email);
}
