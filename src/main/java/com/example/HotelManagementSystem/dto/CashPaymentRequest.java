package com.example.HotelManagementSystem.dto;

public class CashPaymentRequest {

    private Double amount;
    private String transactionId;

    public CashPaymentRequest() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}