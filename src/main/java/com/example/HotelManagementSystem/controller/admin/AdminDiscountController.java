package com.example.HotelManagementSystem.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HotelManagementSystem.entity.Discount;
import com.example.HotelManagementSystem.service.DiscountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/discounts")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class AdminDiscountController {

    // =========================
    // Service Injection
    // =========================

    private final DiscountService discountService;

    public AdminDiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // =========================
    // CREATE DISCOUNT
    // =========================

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDiscount(
            @Valid @RequestBody Discount discount) {

        Discount createdDiscount =
                discountService.createDiscount(discount);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 201);

        response.put("message",
                "Discount created successfully");

        response.put("discount", createdDiscount);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // =========================
    // GET ALL DISCOUNTS
    // =========================

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDiscounts() {

        List<Discount> discounts =
                discountService.getAllDiscounts();

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discounts fetched successfully");

        response.put("totalDiscounts",
                discounts.size());

        response.put("discounts", discounts);

        return ResponseEntity.ok(response);
    }

    // =========================
    // GET ACTIVE DISCOUNTS
    // =========================

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveDiscounts() {

        List<Discount> discounts =
                discountService.getActiveDiscounts();

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Active discounts fetched successfully");

        response.put("totalActiveDiscounts",
                discounts.size());

        response.put("discounts", discounts);

        return ResponseEntity.ok(response);
    }

    // =========================
    // GET DISCOUNT BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDiscountById(
            @PathVariable Long id) {

        Discount discount =
                discountService.getDiscountById(id);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount fetched successfully");

        response.put("discount", discount);

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE DISCOUNT
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDiscount(
            @PathVariable Long id,
            @Valid @RequestBody Discount discount) {

        Discount updatedDiscount =
                discountService.updateDiscount(id, discount);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount updated successfully");

        response.put("discount", updatedDiscount);

        return ResponseEntity.ok(response);
    }

    // =========================
    // DEACTIVATE DISCOUNT
    // =========================

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateDiscount(
            @PathVariable Long id) {

        discountService.deactivateDiscount(id);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount deactivated successfully");

        return ResponseEntity.ok(response);
    }

    // =========================
    // DELETE DISCOUNT
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDiscount(
            @PathVariable Long id) {

        discountService.deleteDiscount(id);

        Map<String, Object> response =
                new HashMap<>();

        response.put("status", 200);

        response.put("message",
                "Discount deleted successfully");

        return ResponseEntity.ok(response);
    }
}
