package org.example.bai1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBContext: quản lý kết nối DB theo cấu hình tập trung.
 *
 * Yêu cầu bài: dùng hằng số cấu hình và đảm bảo mọi phương thức truy vấn
 * đều phải đóng kết nối trong khối finally.
 */
public final class DBContext {

    // ====== Cấu hình tập trung (theo đề bài) ======
    public static final String DB_URL = "jdbc:mysql://192.168.1.10:3306/Hospital_DB";
    public static final String DB_USER = "admin";
    public static final String DB_PASSWORD = "med123";

    /**
     * Với MySQL Connector/J 8+ driver tự đăng ký, nhưng khai báo hằng số để rõ ràng.
     */
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private DBContext() {
        // utility class
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            // chuyển sang unchecked exception để báo lỗi cấu hình sớm
            throw new IllegalStateException("Missing JDBC driver: " + JDBC_DRIVER, e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ====== Helpers đóng tài nguyên an toàn (phục vụ finally) ======
    public static void closeQuietly(AutoCloseable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Exception ignored) {
            // không ném tiếp để tránh che lỗi chính
        }
    }
}

