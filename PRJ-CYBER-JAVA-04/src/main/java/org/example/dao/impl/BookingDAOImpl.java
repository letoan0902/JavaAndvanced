package org.example.dao.impl;

import org.example.dao.BookingDAO;
import org.example.model.Booking;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, p.pc_number, c.name AS category_name, u.full_name AS customer_name " +
                     "FROM bookings b " +
                     "JOIN pcs p ON b.pc_id = p.id " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.user_id = ? ORDER BY b.created_at DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> findByPcId(int pcId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, p.pc_number, c.name AS category_name, u.full_name AS customer_name " +
                     "FROM bookings b " +
                     "JOIN pcs p ON b.pc_id = p.id " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.pc_id = ? AND b.status IN ('PENDING', 'CONFIRMED', 'IN_USE') " +
                     "ORDER BY b.start_time";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pcId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, p.pc_number, c.name AS category_name, u.full_name AS customer_name " +
                     "FROM bookings b " +
                     "JOIN pcs p ON b.pc_id = p.id " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "JOIN users u ON b.user_id = u.id " +
                     "ORDER BY b.created_at DESC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public Booking findById(int id) {
        String sql = "SELECT b.*, p.pc_number, c.name AS category_name, u.full_name AS customer_name " +
                     "FROM bookings b " +
                     "JOIN pcs p ON b.pc_id = p.id " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "JOIN users u ON b.user_id = u.id " +
                     "WHERE b.id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, pc_id, start_time, end_time, total_price, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getPcId());
            pstmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            pstmt.setDouble(5, booking.getTotalPrice());
            pstmt.setString(6, booking.getStatus() != null ? booking.getStatus() : "PENDING");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isOverlapping(int pcId, String startTime, String endTime, int excludeBookingId) {
        // Kiem tra co booking nao cung may, cung khung gio va chua huy khong
        String sql = "SELECT COUNT(*) FROM bookings " +
                     "WHERE pc_id = ? AND id != ? " +
                     "AND status IN ('PENDING', 'CONFIRMED', 'IN_USE') " +
                     "AND start_time < ? AND end_time > ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pcId);
            pstmt.setInt(2, excludeBookingId);
            pstmt.setString(3, endTime);
            pstmt.setString(4, startTime);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // mac dinh la trung de an toan
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Timestamp tsStart = rs.getTimestamp("start_time");
        Timestamp tsEnd = rs.getTimestamp("end_time");
        Timestamp tsCreated = rs.getTimestamp("created_at");

        Booking booking = new Booking(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("pc_id"),
                tsStart != null ? tsStart.toLocalDateTime() : null,
                tsEnd != null ? tsEnd.toLocalDateTime() : null,
                rs.getDouble("total_price"),
                rs.getString("status"),
                tsCreated != null ? tsCreated.toLocalDateTime() : null
        );
        booking.setPcNumber(rs.getString("pc_number"));
        booking.setCategoryName(rs.getString("category_name"));
        booking.setCustomerName(rs.getString("customer_name"));
        return booking;
    }
}
