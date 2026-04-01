package org.example.dao.impl;

import org.example.dao.TransactionDAO;
import org.example.model.Transaction;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.full_name AS customer_name FROM transactions t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "WHERE t.user_id = ? ORDER BY t.created_at DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.full_name AS customer_name FROM transactions t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "ORDER BY t.created_at DESC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public boolean insert(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, type, amount, balance_after, description) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getUserId());
            pstmt.setString(2, transaction.getType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setDouble(4, transaction.getBalanceAfter());
            pstmt.setString(5, transaction.getDescription());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Timestamp tsCreated = rs.getTimestamp("created_at");
        Transaction t = new Transaction(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("type"),
                rs.getDouble("amount"),
                rs.getDouble("balance_after"),
                rs.getString("description"),
                tsCreated != null ? tsCreated.toLocalDateTime() : null
        );
        t.setCustomerName(rs.getString("customer_name"));
        return t;
    }
}
