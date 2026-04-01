package org.example.model;

import java.time.LocalDateTime;

/**
 * User - Entity đại diện cho người dùng hệ thống
 *
 * Vai trò: ADMIN / STAFF / CUSTOMER
 *
 * Áp dụng:
 * - OOP: Đóng gói (private fields + getter/setter)
 * - Java 8: LocalDateTime thay cho Date
 */
public class User {

    private int id;
    private String username;
    private String password; // Đã hash bằng SHA-256
    private String fullName;
    private String phone;
    private double balance; // Số dư tài khoản (VNĐ)
    private String role; // "ADMIN" | "STAFF" | "CUSTOMER"
    private LocalDateTime createdAt;

    public User() {
    }

    public User(int id, String username, String password, String fullName,
            String phone, double balance, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.balance = balance;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String username, String password, String fullName, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.balance = 0.0;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', fullName='%s', role='%s', balance=%.2f}",
                id, username, fullName, role, balance);
    }
}
