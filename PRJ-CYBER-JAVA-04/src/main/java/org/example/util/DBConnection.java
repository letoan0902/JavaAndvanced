package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton Pattern
 * Quản lý kết nối duy nhất đến MySQL Database.
 *
 * Áp dụng:
 * - Design Pattern: Singleton - đảm bảo chỉ 1 instance
 * - JDBC Connection - DriverManager.getConnection()
 * - Exception Handling - try-catch SQLException
 */
public class DBConnection {

    // === Singleton instance ===
    private static DBConnection instance;

    private static final String URL = "jdbc:mysql://localhost:3306/cyber_gaming_db";
    private static final String USER = "root";
    private static final String PASS = "123456";

    private Connection connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("[OK] Kết nối Database thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("[LỖI] Không tìm thấy MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[LỖI] Lỗi kết nối Database!");
            e.printStackTrace();
        }
    }

    //Nếu connection bị đóng hoặc null → tạo lại
    public static DBConnection getInstance() {
        try {
            if (instance == null || instance.connection == null || instance.connection.isClosed()) {
                instance = new DBConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[OK] Đã đóng kết nối Database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
