package org.example.dao.impl;

import org.example.dao.CategoryDAO;
import org.example.model.Category;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
