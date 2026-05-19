package com.example.HotelManagementSystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Discount Code
    // =========================

    @NotBlank(message = "Discount code is required")
    @Column(nullable = false, unique = true)
    private String code;

    // =========================
    // Description
    // =========================

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    // =========================
    // Discount Type (PERCENTAGE or FIXED)
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    // =========================
    // Discount Value
    // =========================

    @NotNull(message = "Discount value is required")
    @Min(value = 0, message = "Discount value must be greater than or equal to 0")
    @Max(value = 100, message = "Percentage cannot exceed 100")
    @Column(nullable = false)
    private Double discountValue;

    // =========================
    // Valid From Date
    // =========================

    @NotNull(message = "Valid from date is required")
    @Column(nullable = false)
    private LocalDate validFrom;

    // =========================
    // Valid To Date
    // =========================

    @NotNull(message = "Valid to date is required")
    @Column(nullable = false)
    private LocalDate validTo;

    // =========================
    // Maximum Uses
    // =========================

    @NotNull(message = "Maximum uses is required")
    @Min(value = 1, message = "Maximum uses must be at least 1")
    @Column(nullable = false)
    private Integer maxUses;

    // =========================
    // Current Uses Count
    // =========================

    @Column(nullable = false)
    private Integer currentUses = 0;

    // =========================
    // Is Active
    // =========================

    @Column(nullable = false)
    private Boolean isActive = true;

    // =========================
    // Minimum Booking Amount
    // =========================

    @Column(nullable = false)
    private Double minBookingAmount = 0.0;

    // =========================
    // Created At
    // =========================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // =========================
    // Relationships
    // =========================

    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DiscountUsage> usages;

    // =========================
    // Constructors
    // =========================

    public Discount() {
    }

    public Discount(
            String code,
            String description,
            DiscountType discountType,
            Double discountValue,
            LocalDate validFrom,
            LocalDate validTo,
            Integer maxUses) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.maxUses = maxUses;
        this.currentUses = 0;
        this.isActive = true;
    }

    // =========================
    // Lifecycle
    // =========================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getCurrentUses() {
        return currentUses;
    }

    public void setCurrentUses(Integer currentUses) {
        this.currentUses = currentUses;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getMinBookingAmount() {
        return minBookingAmount;
    }

    public void setMinBookingAmount(Double minBookingAmount) {
        this.minBookingAmount = minBookingAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<DiscountUsage> getUsages() {
        return usages;
    }

    public void setUsages(List<DiscountUsage> usages) {
        this.usages = usages;
    }
}
