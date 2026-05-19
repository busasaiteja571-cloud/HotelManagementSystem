package com.example.HotelManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.DiscountUsage;

public interface DiscountUsageRepository extends JpaRepository<DiscountUsage, Long> {

    List<DiscountUsage> findByUserId(Long userId);

    List<DiscountUsage> findByDiscountId(Long discountId);

    List<DiscountUsage> findByBookingId(Long bookingId);
}
