package com.example.HotelManagementSystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HotelManagementSystem.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCode(String code);

    List<Discount> findByIsActiveTrue();

    List<Discount> findByValidFromBeforeAndValidToAfterAndIsActiveTrue(
            LocalDate validFrom,
            LocalDate validTo);

    List<Discount> findByIsActiveFalse();
}
