package org.example.dao;

import org.example.model.Order;
import org.example.model.OrderDetail;
import java.util.List;

// Interface DAO cho Order + OrderDetail (don hang F&B)
public interface OrderDAO {
    List<Order> findByUserId(int userId);
    List<Order> findAll();
    Order findById(int id);
    int insert(Order order);  // tra ve generated id
    boolean insertOrderDetail(int orderId, OrderDetail detail);
    List<OrderDetail> findDetailsByOrderId(int orderId);
    boolean updateStatus(int id, String status);
}
