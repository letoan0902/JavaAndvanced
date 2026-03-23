package org.example.bai4;

import org.example.bai1.DBContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bai4 {

    /*
     * PHẦN 1 - PHÂN TÍCH:
     *
     * Code lỗi thường kiểu:
     *   String patientName = input;
     *   String sql = "SELECT * FROM Patients WHERE full_name = '" + patientName + "'";
     *
     * Nếu attacker nhập:  ' OR '1'='1'; --
     * => sql sau khi nối chuỗi trở thành:
     *   SELECT * FROM Patients WHERE full_name = '' OR '1'='1'; --'
     *
     * Giải thích vì sao WHERE luôn đúng (true):
     * - Điều kiện WHERE gồm 2 vế nối bởi OR:
     *     full_name = ''   OR   '1'='1'
     * - '1'='1' luôn là TRUE, nên cả biểu thức luôn TRUE bất kể full_name là gì.
     * - Phần "--" biến phần còn lại của câu lệnh thành comment (tùy DB), giúp tránh lỗi dấu nháy.
     * Kết quả: query trả về TẤT CẢ bệnh nhân thay vì 1 người.
     */

    /*
     * PHẦN 2 - THỰC THI (theo yêu cầu đề):
     */

    public static String sanitizePatientName(String input) {
        if (input == null) return "";

        String s = input;

        // loại bỏ comment injection
        s = s.replace("--", "");

        // loại bỏ statement terminator
        s = s.replace(";", "");

        // loại bỏ dấu nháy đơn để tránh đóng chuỗi
        s = s.replace("'", "");

        // có thể trim để giảm rác
        return s.trim();
    }

    public static void findPatientsByNameUsingStatement(String rawNameInput) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String safeName = sanitizePatientName(rawNameInput);

        // vẫn dùng Statement (theo đề), nhưng đã lọc ký tự đặc biệt trước khi nối chuỗi
        String sql = "SELECT patient_id, full_name FROM Patients WHERE full_name = '" + safeName + "'";

        try {
            conn = DBContext.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            boolean hasAny = false;
            while (rs.next()) {
                hasAny = true;
                String id = rs.getString("patient_id");
                String name = rs.getString("full_name");
                System.out.println(id + " | " + name);
            }

            if (!hasAny) {
                System.out.println("Không tìm thấy bệnh nhân với tên: " + safeName);
            }

        } catch (SQLException e) {
            System.err.println("DB error while searching patient: " + e.getMessage());
        } finally {
            DBContext.closeQuietly(rs);
            DBContext.closeQuietly(stmt);
            DBContext.closeQuietly(conn);
        }
    }

    public static void main(String[] args) {
        String input = (args != null && args.length > 0) ? args[0] : "' OR '1'='1'; --";
        System.out.println("Raw input: " + input);
        System.out.println("Sanitized: " + sanitizePatientName(input));
        findPatientsByNameUsingStatement(input);
    }
}


