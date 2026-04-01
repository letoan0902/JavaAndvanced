package org.example.service;

import org.example.util.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

// ReportService - Bao cao doanh thu tu booking va order
public class ReportService implements IReportService {

    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    // Bao cao doanh thu theo khoang thoi gian
    @Override
    public Map<String, Object> getRevenueReport(String fromDate, String toDate) {
        Map<String, Object> report = new HashMap<>();

        // Doanh thu booking
        String sqlBooking = "SELECT COUNT(*) AS total_bookings, COALESCE(SUM(total_price), 0) AS booking_revenue " +
                            "FROM bookings WHERE status = 'COMPLETED' " +
                            "AND DATE(created_at) BETWEEN ? AND ?";

        // Doanh thu order
        String sqlOrder = "SELECT COUNT(*) AS total_orders, COALESCE(SUM(total_price), 0) AS order_revenue " +
                          "FROM orders WHERE status = 'COMPLETED' " +
                          "AND DATE(created_at) BETWEEN ? AND ?";

        // Tong nap tien
        String sqlDeposit = "SELECT COALESCE(SUM(amount), 0) AS total_deposit " +
                            "FROM transactions WHERE type = 'DEPOSIT' " +
                            "AND DATE(created_at) BETWEEN ? AND ?";

        try {
            // Booking revenue
            try (PreparedStatement pstmt = getConnection().prepareStatement(sqlBooking)) {
                pstmt.setString(1, fromDate);
                pstmt.setString(2, toDate);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    report.put("totalBookings", rs.getInt("total_bookings"));
                    report.put("bookingRevenue", rs.getDouble("booking_revenue"));
                }
            }

            // Order revenue
            try (PreparedStatement pstmt = getConnection().prepareStatement(sqlOrder)) {
                pstmt.setString(1, fromDate);
                pstmt.setString(2, toDate);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    report.put("totalOrders", rs.getInt("total_orders"));
                    report.put("orderRevenue", rs.getDouble("order_revenue"));
                }
            }

            // Deposit total
            try (PreparedStatement pstmt = getConnection().prepareStatement(sqlDeposit)) {
                pstmt.setString(1, fromDate);
                pstmt.setString(2, toDate);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    report.put("totalDeposit", rs.getDouble("total_deposit"));
                }
            }

            // Tong doanh thu
            double bookingRev = (double) report.getOrDefault("bookingRevenue", 0.0);
            double orderRev = (double) report.getOrDefault("orderRevenue", 0.0);
            report.put("totalRevenue", bookingRev + orderRev);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }

    // Bao cao trong ngay
    @Override
    public Map<String, Object> getDailyReport(String date) {
        return getRevenueReport(date, date);
    }

    // Thong ke tong quat (toan bo thoi gian)
    @Override
    public Map<String, Object> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // So luong user theo role
            String sqlUsers = "SELECT role, COUNT(*) AS cnt FROM users GROUP BY role";
            try (Statement stmt = getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sqlUsers)) {
                while (rs.next()) {
                    stats.put("users_" + rs.getString("role").toLowerCase(), rs.getInt("cnt"));
                }
            }

            // So luong may tram theo trang thai
            String sqlPCs = "SELECT status, COUNT(*) AS cnt FROM pcs GROUP BY status";
            try (Statement stmt = getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sqlPCs)) {
                while (rs.next()) {
                    stats.put("pcs_" + rs.getString("status").toLowerCase(), rs.getInt("cnt"));
                }
            }

            // Tong so booking va doanh thu
            String sqlBookings = "SELECT COUNT(*) AS total, " +
                                 "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed, " +
                                 "COALESCE(SUM(CASE WHEN status = 'COMPLETED' THEN total_price ELSE 0 END), 0) AS revenue " +
                                 "FROM bookings";
            try (Statement stmt = getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sqlBookings)) {
                if (rs.next()) {
                    stats.put("bookings_total", rs.getInt("total"));
                    stats.put("bookings_completed", rs.getInt("completed"));
                    stats.put("bookings_revenue", rs.getDouble("revenue"));
                }
            }

            // Tong so order va doanh thu
            String sqlOrders = "SELECT COUNT(*) AS total, " +
                               "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed, " +
                               "COALESCE(SUM(CASE WHEN status = 'COMPLETED' THEN total_price ELSE 0 END), 0) AS revenue " +
                               "FROM orders";
            try (Statement stmt = getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sqlOrders)) {
                if (rs.next()) {
                    stats.put("orders_total", rs.getInt("total"));
                    stats.put("orders_completed", rs.getInt("completed"));
                    stats.put("orders_revenue", rs.getDouble("revenue"));
                }
            }

            // Top 3 mon ban chay nhat
            String sqlTopFoods = "SELECT f.name, SUM(od.quantity) AS total_qty " +
                                 "FROM order_details od JOIN foods f ON od.food_id = f.id " +
                                 "JOIN orders o ON od.order_id = o.id " +
                                 "WHERE o.status != 'CANCELLED' " +
                                 "GROUP BY f.id, f.name ORDER BY total_qty DESC LIMIT 3";
            StringBuilder topFoods = new StringBuilder();
            try (Statement stmt = getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sqlTopFoods)) {
                int rank = 1;
                while (rs.next()) {
                    topFoods.append(String.format("  %d. %s (%d da ban)\n",
                            rank++, rs.getString("name"), rs.getInt("total_qty")));
                }
            }
            stats.put("topFoods", topFoods.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
}
