package com.example.HotelManagementSystem.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.ApplyDiscountRequest;
import com.example.HotelManagementSystem.entity.Discount;
import com.example.HotelManagementSystem.entity.DiscountUsage;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.DiscountService;
import com.example.HotelManagementSystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/discounts")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class UserDiscountController {

    // =========================
    // Service Injection
    // =========================

    private final DiscountService discountService;

    private final UserService userService;

    public UserDiscountController(
            DiscountService discountService,
            UserService userService) {
        this.discountService = discountService;
        this.userService = userService;
    }

    // =========================
    // GET AVAILABLE DISCOUNTS
    // =========================

    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableDiscounts() {

        List<Discount> discounts =
                discountService.getAvailableDiscounts();

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Available discounts fetched successfully");

        response.put("totalAvailable",
                discounts.size());

        response.put("discounts", discounts);

        return ResponseEntity.ok(response);
    }

    // =========================
    // VALIDATE DISCOUNT CODE
    // =========================

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDiscount(
            @Valid @RequestBody ApplyDiscountRequest request) {

        boolean isValid =
                discountService.validateDiscountCode(
                        request.getDiscountCode(),
                        request.getBookingAmount());

        Map<String, Object> response =
                new HashMap<>();

        if (isValid) {

            Double discountAmount =
                    discountService.calculateDiscountAmount(
                            request.getDiscountCode(),
                            request.getBookingAmount());

            response.put("status", 200);

            response.put("message",
                    "Discount code is valid");

            response.put("isValid", true);

            response.put("discountAmount",
                    discountAmount);

            response.put("finalAmount",
                    request.getBookingAmount() -
                    discountAmount);

        } else {

            response.put("status", 400);

            response.put("message",
                    "Discount code is invalid or not applicable");

            response.put("isValid", false);
        }

        return ResponseEntity.ok(response);
    }

    // =========================
    // APPLY DISCOUNT
    // =========================

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyDiscount(
            @Valid @RequestBody ApplyDiscountRequest request) {

        User user =
                userService.getUserById(
                        request.getUserId());

        DiscountUsage usage =
                discountService.applyDiscount(
                        request.getDiscountCode(),
                        user,
                        request.getBookingAmount());

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount applied successfully");

        response.put("originalAmount",
                usage.getOriginalAmount());

        response.put("discountAmountApplied",
                usage.getDiscountAmountApplied());

        response.put("finalAmount",
                usage.getFinalAmount());

        response.put("discountCode",
                usage.getDiscount().getCode());

        response.put("usageId", usage.getId());

        return ResponseEntity.ok(response);
    }

    // =========================
    // GET MY DISCOUNT USAGE HISTORY
    // =========================

    @GetMapping("/history/{userId}")
    public ResponseEntity<Map<String, Object>> getMyDiscountHistory(
            Long userId) {

        List<DiscountUsage> usages =
                discountService.getDiscountUsageForUser(userId);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount usage history fetched successfully");

        response.put("totalUsages", usages.size());

        response.put("usages", usages);

        return ResponseEntity.ok(response);
    }
}
