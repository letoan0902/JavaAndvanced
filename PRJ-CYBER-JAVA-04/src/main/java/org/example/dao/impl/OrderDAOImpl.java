package org.example.dao.impl;

import org.example.dao.OrderDAO;
import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS customer_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id " +
                     "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderDetails(findDetailsByOrderId(order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS customer_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id " +
                     "ORDER BY o.created_at DESC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderDetails(findDetailsByOrderId(order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order findById(int id) {
        String sql = "SELECT o.*, u.full_name AS customer_name FROM orders o " +
                     "JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderDetails(findDetailsByOrderId(order.getId()));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(Order order) {
        String sql = "INSERT INTO orders (user_id, booking_id, total_price, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getUserId());
            if (order.getBookingId() != null) {
                pstmt.setInt(2, order.getBookingId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getStatus() != null ? order.getStatus() : "PENDING");

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean insertOrderDetail(int orderId, OrderDetail detail) {
        String sql = "INSERT INTO order_details (order_id, food_id, quantity, unit_price, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, detail.getFoodId());
            pstmt.setInt(3, detail.getQuantity());
            pstmt.setDouble(4, detail.getUnitPrice());
            pstmt.setDouble(5, detail.getSubtotal());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderDetail> findDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT od.*, f.name AS food_name FROM order_details od " +
                     "JOIN foods f ON od.food_id = f.id WHERE od.order_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                details.add(new OrderDetail(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("food_id"),
                        rs.getString("food_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Timestamp tsCreated = rs.getTimestamp("created_at");
        int bookingId = rs.getInt("booking_id");

        Order order = new Order(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.wasNull() ? null : bookingId,
                rs.getDouble("total_price"),
                rs.getString("status"),
                tsCreated != null ? tsCreated.toLocalDateTime() : null
        );
        order.setCustomerName(rs.getString("customer_name"));
        return order;
    }
}
