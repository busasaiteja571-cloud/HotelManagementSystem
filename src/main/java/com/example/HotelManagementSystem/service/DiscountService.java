package com.example.HotelManagementSystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.entity.Discount;
import com.example.HotelManagementSystem.entity.DiscountType;
import com.example.HotelManagementSystem.entity.DiscountUsage;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.repository.DiscountRepository;
import com.example.HotelManagementSystem.repository.DiscountUsageRepository;

@Service
public class DiscountService {

    // =========================
    // Repository Injection
    // =========================

    private final DiscountRepository discountRepository;

    private final DiscountUsageRepository discountUsageRepository;

    // =========================
    // Constructor Injection
    // =========================

    public DiscountService(
            DiscountRepository discountRepository,
            DiscountUsageRepository discountUsageRepository) {
        this.discountRepository = discountRepository;
        this.discountUsageRepository = discountUsageRepository;
    }

    // =========================
    // CREATE DISCOUNT (Admin)
    // =========================

    public Discount createDiscount(Discount discount) {

        // Validate validity period
        if (discount.getValidFrom().isAfter(discount.getValidTo())) {

            throw new RuntimeException(
                    "Valid from date must be before valid to date");
        }

        // Check if code already exists
        discountRepository.findByCode(discount.getCode())
                .ifPresent(existingDiscount -> {

                    throw new RuntimeException(
                            "Discount code already exists");
                });

        return discountRepository.save(discount);
    }

    // =========================
    // GET ALL DISCOUNTS
    // =========================

    public List<Discount> getAllDiscounts() {

        return discountRepository.findAll();
    }

    // =========================
    // GET ACTIVE DISCOUNTS
    // =========================

    public List<Discount> getActiveDiscounts() {

        return discountRepository.findByIsActiveTrue();
    }

    // =========================
    // GET AVAILABLE DISCOUNTS FOR TODAY
    // =========================

    public List<Discount> getAvailableDiscounts() {

        LocalDate today = LocalDate.now();

        return discountRepository
                .findByValidFromBeforeAndValidToAfterAndIsActiveTrue(
                        today,
                        today);
    }

    // =========================
    // GET DISCOUNT BY CODE
    // =========================

    public Discount getDiscountByCode(String code) {

        return discountRepository.findByCode(code)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Discount not found"));
    }

    // =========================
    // GET DISCOUNT BY ID
    // =========================

    public Discount getDiscountById(Long id) {

        return discountRepository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Discount not found"));
    }

    // =========================
    // VALIDATE DISCOUNT CODE
    // =========================

    public boolean validateDiscountCode(
            String code,
            Double bookingAmount) {

        Discount discount =
                getDiscountByCode(code);

        LocalDate today = LocalDate.now();

        // Check if discount is active
        if (!discount.getIsActive()) {

            return false;
        }

        // Check validity period
        if (today.isBefore(discount.getValidFrom()) ||
                today.isAfter(discount.getValidTo())) {

            return false;
        }

        // Check if max uses reached
        if (discount.getCurrentUses() >=
                discount.getMaxUses()) {

            return false;
        }

        // Check minimum booking amount
        if (bookingAmount <
                discount.getMinBookingAmount()) {

            return false;
        }

        return true;
    }

    // =========================
    // CALCULATE DISCOUNT AMOUNT
    // =========================

    public Double calculateDiscountAmount(
            String code,
            Double bookingAmount) {

        Discount discount =
                getDiscountByCode(code);

        if (discount.getDiscountType() ==
                DiscountType.PERCENTAGE) {

            return (bookingAmount *
                    discount.getDiscountValue()) / 100;

        } else {

            // FIXED discount
            return Math.min(
                    discount.getDiscountValue(),
                    bookingAmount);
        }
    }

    // =========================
    // APPLY DISCOUNT
    // =========================

    public DiscountUsage applyDiscount(
            String code,
            User user,
            Double originalAmount) {

        // Validate discount
        if (!validateDiscountCode(code, originalAmount)) {

            throw new RuntimeException(
                    "Discount code is invalid or not applicable");
        }

        Discount discount =
                getDiscountByCode(code);

        // Calculate discount amount
        Double discountAmountApplied =
                calculateDiscountAmount(
                        code,
                        originalAmount);

        // Calculate final amount
        Double finalAmount = originalAmount -
                discountAmountApplied;

        // Create discount usage record
        DiscountUsage usage =
                new DiscountUsage(
                        discount,
                        user,
                        discountAmountApplied,
                        originalAmount,
                        finalAmount);

        // Increment current uses
        discount.setCurrentUses(
                discount.getCurrentUses() + 1);

        // Check if max uses reached and deactivate
        if (discount.getCurrentUses() >=
                discount.getMaxUses()) {

            discount.setIsActive(false);
        }

        discountRepository.save(discount);

        return discountUsageRepository.save(usage);
    }

    // =========================
    // UPDATE DISCOUNT (Admin)
    // =========================

    public Discount updateDiscount(
            Long id,
            Discount updatedDiscount) {

        Discount discount =
                getDiscountById(id);

        discount.setDescription(
                updatedDiscount.getDescription());

        discount.setDiscountValue(
                updatedDiscount.getDiscountValue());

        discount.setValidFrom(
                updatedDiscount.getValidFrom());

        discount.setValidTo(
                updatedDiscount.getValidTo());

        discount.setMaxUses(
                updatedDiscount.getMaxUses());

        discount.setIsActive(
                updatedDiscount.getIsActive());

        discount.setMinBookingAmount(
                updatedDiscount.getMinBookingAmount());

        return discountRepository.save(discount);
    }

    // =========================
    // DEACTIVATE DISCOUNT (Admin)
    // =========================

    public void deactivateDiscount(Long id) {

        Discount discount =
                getDiscountById(id);

        discount.setIsActive(false);

        discountRepository.save(discount);
    }

    // =========================
    // GET DISCOUNT USAGE FOR USER
    // =========================

    public List<DiscountUsage> getDiscountUsageForUser(
            Long userId) {

        return discountUsageRepository.findByUserId(userId);
    }

    // =========================
    // GET DISCOUNT USAGE COUNT
    // =========================

    public Integer getDiscountUsageCount(Long discountId) {

        List<DiscountUsage> usages =
                discountUsageRepository
                .findByDiscountId(discountId);

        return usages.size();
    }

    // =========================
    // DELETE DISCOUNT (Admin)
    // =========================

    public void deleteDiscount(Long id) {

        Discount discount =
                getDiscountById(id);

        discountRepository.delete(discount);
    }
}
