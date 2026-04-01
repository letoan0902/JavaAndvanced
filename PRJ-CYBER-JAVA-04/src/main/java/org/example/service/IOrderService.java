package org.example.service;

import org.example.model.Food;
import org.example.model.Order;
import org.example.model.OrderDetail;
import java.util.List;

// Interface cho OrderService - ap dung DIP (SOLID)
public interface IOrderService {
    List<Food> getAvailableFoods();
    List<Order> getOrdersByUser(int userId);
    List<Order> getAllOrders();
    Order getOrderById(int id);
    String createOrder(int userId, Integer bookingId, List<OrderDetail> items);
    boolean cancelOrder(int orderId, int userId);
}
