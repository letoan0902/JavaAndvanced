package org.example.model;

import java.time.LocalDateTime;

/**
 * Booking - Entity đại diện cho đơn đặt máy trạm
 *
 * Workflow trạng thái:
 * PENDING → CONFIRMED → IN_USE → COMPLETED
 * → CANCELLED
 */
public class Booking {

    private int id;
    private int userId;
    private int pcId;
    private String pcNumber; // Transient - để hiển thị
    private String categoryName; // Transient - để hiển thị
    private String customerName; // Transient - để hiển thị
    private LocalDateTime startTime;
    private LocalDateTime endTime; // Thời gian kết thúc dự kiến
    private double totalPrice; // Tổng giá = số giờ × giá/giờ
    private String status; // "PENDING" | "CONFIRMED" | "IN_USE" | "COMPLETED" | "CANCELLED"
    private LocalDateTime createdAt;


    public Booking() {
    }

    public Booking(int id, int userId, int pcId, LocalDateTime startTime,
            LocalDateTime endTime, double totalPrice, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.pcId = pcId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Booking(int userId, int pcId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice) {
        this.userId = userId;
        this.pcId = pcId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = "PENDING";
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

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public String getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(String pcNumber) {
        this.pcNumber = pcNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Booking{id=%d, userId=%d, pcId=%d, status='%s', total=%.0f}",
                id, userId, pcId, status, totalPrice);
    }
}
