package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order - Entity đại diện cho đơn hàng F&B
 *
 * Workflow trạng thái:
 * PENDING → PREPARING → SERVED → COMPLETED
 * → CANCELLED
 *
 * Áp dụng:
 * - Java Collection: List<OrderDetail> chứa chi tiết đơn hàng
 */
public class Order {

    private int id;
    private int userId;
    private Integer bookingId; // Nullable - đơn F&B có thể không gắn booking
    private String customerName; // Transient - để hiển thị
    private double totalPrice;
    private String status; // "PENDING" | "PREPARING" | "SERVED" | "COMPLETED" | "CANCELLED"
    private LocalDateTime createdAt;

    // Danh sách chi tiết đơn hàng
    private List<OrderDetail> orderDetails;


    public Order() {
        this.orderDetails = new ArrayList<>();
    }

    public Order(int id, int userId, Integer bookingId, double totalPrice,
            String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.bookingId = bookingId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.orderDetails = new ArrayList<>();
    }

    // Constructor khi tạo đơn hàng mới
    public Order(int userId, Integer bookingId) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.totalPrice = 0.0;
        this.status = "PENDING";
        this.orderDetails = new ArrayList<>();
    }

    // Business Methods

    // Thêm chi tiết đơn hàng và cập nhật tổng tiền
    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
        this.totalPrice += detail.getSubtotal();
    }

    // Tính lại tổng tiền từ danh sách chi tiết
    public void recalculateTotal() {
        this.totalPrice = orderDetails.stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();
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

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, userId=%d, items=%d, total=%.0f, status='%s'}",
                id, userId, orderDetails.size(), totalPrice, status);
    }
}
