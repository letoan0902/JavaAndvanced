package org.example.bai2;

import org.example.bai1.DBContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bai2 {
    /*
     * PHẦN 1 - PHÂN TÍCH:
     *
     * - if (rs.next()) chỉ phù hợp khi bạn chỉ cần xử lý 1 dòng (ví dụ: đọc bản ghi đầu tiên).
     *   Yêu cầu bài này là "in danh sách" => phải duyệt TOÀN BỘ các dòng => cần vòng lặp (while/do-while).
     *
     * - ResultSet hoạt động như một con trỏ (cursor) trỏ vào các dòng kết quả:
     *   + Ban đầu con trỏ đứng TRƯỚC dòng đầu tiên.
     *   + Mỗi lần gọi rs.next(): con trỏ DI CHUYỂN sang dòng kế tiếp.
     *       * Nếu có dòng: trả về true và bạn có thể đọc cột ở dòng đó.
     *       * Nếu hết dòng hoặc bảng trống: trả về false.
     *   => Nếu chỉ dùng if (rs.next()) thì bạn chỉ đọc được đúng 1 dòng (dòng đầu tiên) rồi kết thúc.
     *   => Nếu bảng trống, rs.next() trả false và khối if không chạy (thường gây "không in gì" hoặc code khác dễ lỗi).
     *
     * PHẦN 2 - THỰC THI:
     */
    public static void printPharmacyCatalogue() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        final String sql = "SELECT medicine_name, stock FROM Pharmacy";

        try {
            conn = DBContext.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            System.out.println("=== PHARMACY CATALOGUE ===");
            System.out.printf("%-30s | %s%n", "Medicine Name", "Stock");
            System.out.println("-----------------------------------------------");

            boolean hasAnyRow = false;
            while (rs.next()) {
                hasAnyRow = true;
                String name = rs.getString("medicine_name");
                int stock = rs.getInt("stock");
                System.out.printf("%-30s | %d%n", name, stock);
            }

            if (!hasAnyRow) {
                System.out.println("(Catalogue is empty)");
            }

        } catch (SQLException e) {
            System.err.println("DB error while loading pharmacy catalogue: " + e.getMessage());
        } finally {
            DBContext.closeQuietly(rs);
            DBContext.closeQuietly(stmt);
            DBContext.closeQuietly(conn);
        }
    }

    public static void main(String[] args) {
        printPharmacyCatalogue();
    }
}



