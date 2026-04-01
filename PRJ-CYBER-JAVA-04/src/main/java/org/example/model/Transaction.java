package org.example.model;

import java.time.LocalDateTime;

/**
 * Transaction - Entity đại diện cho lịch sử giao dịch tài khoản
 *
 * Loại giao dịch: DEPOSIT / BOOKING_PAYMENT / ORDER_PAYMENT / REFUND
 *
 * Dùng cho tính năng nâng cao: Ví điện tử nội bộ
 */
public class Transaction {

    private int id;
    private int userId;
    private String type; // "DEPOSIT" | "BOOKING_PAYMENT" | "ORDER_PAYMENT" | "REFUND"
    private double amount; // Số tiền giao dịch
    private double balanceAfter; // Số dư sau giao dịch
    private String description; // Mô tả giao dịch
    private LocalDateTime createdAt;

    // Transient
    private String customerName; // Để hiển thị

    public Transaction() {
    }

    public Transaction(int id, int userId, String type, double amount,
            double balanceAfter, String description, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdAt = createdAt;
    }


    public Transaction(int userId, String type, double amount, double balanceAfter, String description) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%d, type='%s', amount=%.0f, balanceAfter=%.0f}",
                id, type, amount, balanceAfter);
    }
}
