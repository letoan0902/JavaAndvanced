package org.example.dao.impl;

import org.example.dao.FoodDAO;
import org.example.model.Food;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAOImpl implements FoodDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Food> findAll() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                foods.add(mapResultSetToFood(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    @Override
    public List<Food> findAvailable() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods WHERE status = 'AVAILABLE' AND stock_quantity > 0 ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                foods.add(mapResultSetToFood(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    @Override
    public Food findById(int id) {
        String sql = "SELECT * FROM foods WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFood(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Food food) {
        String sql = "INSERT INTO foods (name, description, price, stock_quantity, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, food.getName());
            pstmt.setString(2, food.getDescription());
            pstmt.setDouble(3, food.getPrice());
            pstmt.setInt(4, food.getStockQuantity());
            pstmt.setString(5, food.getStatus() != null ? food.getStatus() : "AVAILABLE");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Food food) {
        String sql = "UPDATE foods SET name = ?, description = ?, price = ?, stock_quantity = ?, status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, food.getName());
            pstmt.setString(2, food.getDescription());
            pstmt.setDouble(3, food.getPrice());
            pstmt.setInt(4, food.getStockQuantity());
            pstmt.setString(5, food.getStatus());
            pstmt.setInt(6, food.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM foods WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStock(int id, int newQuantity) {
        String sql = "UPDATE foods SET stock_quantity = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Food mapResultSetToFood(ResultSet rs) throws SQLException {
        return new Food(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("stock_quantity"),
                rs.getString("status")
        );
    }
}
