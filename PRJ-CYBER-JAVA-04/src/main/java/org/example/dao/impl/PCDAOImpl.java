package org.example.dao.impl;

import org.example.dao.PCDAO;
import org.example.model.PC;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PCDAOImpl implements PCDAO {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<PC> findAll() {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM pcs p " +
                     "JOIN categories c ON p.category_id = c.id ORDER BY p.id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcs;
    }

    @Override
    public List<PC> findByCategoryId(int categoryId) {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM pcs p " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.category_id = ? ORDER BY p.id";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcs;
    }

    @Override
    public List<PC> findAvailable() {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM pcs p " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.status = 'AVAILABLE' ORDER BY c.id, p.id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pcs.add(mapResultSetToPC(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcs;
    }

    @Override
    public PC findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name FROM pcs p " +
                     "JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPC(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(PC pc) {
        String sql = "INSERT INTO pcs (pc_number, category_id, config_info, price_per_hour, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, pc.getPcNumber());
            pstmt.setInt(2, pc.getCategoryId());
            pstmt.setString(3, pc.getConfigInfo());
            pstmt.setDouble(4, pc.getPricePerHour());
            pstmt.setString(5, pc.getStatus() != null ? pc.getStatus() : "AVAILABLE");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(PC pc) {
        String sql = "UPDATE pcs SET pc_number = ?, category_id = ?, config_info = ?, " +
                     "price_per_hour = ?, status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, pc.getPcNumber());
            pstmt.setInt(2, pc.getCategoryId());
            pstmt.setString(3, pc.getConfigInfo());
            pstmt.setDouble(4, pc.getPricePerHour());
            pstmt.setString(5, pc.getStatus());
            pstmt.setInt(6, pc.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pcs WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE pcs SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PC mapResultSetToPC(ResultSet rs) throws SQLException {
        return new PC(
                rs.getInt("id"),
                rs.getString("pc_number"),
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getString("config_info"),
                rs.getDouble("price_per_hour"),
                rs.getString("status")
        );
    }
}
