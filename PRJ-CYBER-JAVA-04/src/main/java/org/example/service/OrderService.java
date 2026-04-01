package org.example.service;

import org.example.dao.FoodDAO;
import org.example.dao.OrderDAO;
import org.example.dao.impl.FoodDAOImpl;
import org.example.dao.impl.OrderDAOImpl;
import org.example.model.Food;
import org.example.model.Order;
import org.example.model.OrderDetail;

import java.util.List;

// OrderService - Xu ly logic don hang F&B
public class OrderService implements IOrderService {

    private final OrderDAO orderDAO;
    private final FoodDAO foodDAO;

    public OrderService() {
        this.orderDAO = new OrderDAOImpl();
        this.foodDAO = new FoodDAOImpl();
    }

    @Override
    public List<Food> getAvailableFoods() {
        return foodDAO.findAvailable();
    }

    @Override
    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    // Tao don hang moi - tra ve thong bao ket qua
    @Override
    public String createOrder(int userId, Integer bookingId, List<OrderDetail> items) {
        if (items == null || items.isEmpty()) {
            return "Đơn hàng phải có ít nhất 1 món!";
        }

        // Validate tung mon: kiem tra ton kho
        double totalPrice = 0;
        for (OrderDetail item : items) {
            Food food = foodDAO.findById(item.getFoodId());
            if (food == null) {
                return "Không tìm thấy món với ID = " + item.getFoodId();
            }
            if (food.getStockQuantity() < item.getQuantity()) {
                return "Món \"" + food.getName() + "\" chỉ còn " + food.getStockQuantity() + " sản phẩm!";
            }
            totalPrice += item.getSubtotal();
        }

        // Kiem tra so du truoc khi dat
        org.example.dao.UserDAO userDAO = new org.example.dao.impl.UserDAOImpl();
        org.example.model.User user = userDAO.findById(userId);
        if (user == null) {
            return "Không tìm thấy tài khoản!";
        }
        if (user.getBalance() < totalPrice) {
            return String.format("Số dư không đủ! Cần %,.0f VND, hiện có %,.0f VND. Vui lòng nạp thêm tiền.",
                    totalPrice, user.getBalance());
        }

        // Tao order
        Order order = new Order(userId, bookingId);
        for (OrderDetail item : items) {
            order.addOrderDetail(item);
        }

        int orderId = orderDAO.insert(order);
        if (orderId <= 0) {
            return "Tạo đơn hàng thất bại!";
        }

        // Luu tung chi tiet va tru ton kho
        for (OrderDetail item : items) {
            orderDAO.insertOrderDetail(orderId, item);
            Food food = foodDAO.findById(item.getFoodId());
            if (food != null) {
                foodDAO.updateStock(food.getId(), food.getStockQuantity() - item.getQuantity());
            }
        }

        return String.format("Đặt đơn hàng #%d thành công! Tổng tiền: %,.0f VND. Tiền sẽ được trừ khi nhân viên chuẩn bị.",
                orderId, order.getTotalPrice());
    }

    // Huy don hang
    @Override
    public boolean cancelOrder(int orderId, int userId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) return false;
        if (order.getUserId() != userId) return false;
        if (!order.getStatus().equals("PENDING")) {
            return false; // chi huy duoc khi PENDING
        }

        // Hoan lai ton kho
        for (OrderDetail item : order.getOrderDetails()) {
            Food food = foodDAO.findById(item.getFoodId());
            if (food != null) {
                foodDAO.updateStock(food.getId(), food.getStockQuantity() + item.getQuantity());
            }
        }

        return orderDAO.updateStatus(orderId, "CANCELLED");
    }
}
