package org.example.dao.impl;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password, full_name, phone, balance, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getPhone());
            pstmt.setDouble(5, user.getBalance());
            pstmt.setString(6, user.getRole());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, balance = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPhone());
            pstmt.setDouble(3, user.getBalance());
            pstmt.setInt(4, user.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBalance(int userId, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Map 1 dong ResultSet thanh doi tuong User
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;

        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getDouble("balance"),
                rs.getString("role"),
                createdAt
        );
    }
}
