package org.example.bai1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bai1 {

    /**
     * PHẦN 1 - PHÂN TÍCH:
     *
     * - Nếu mỗi lần truy vấn đều mở Connection mới nhưng không close():
     *   1) RÒ RỈ TÀI NGUYÊN (resource leak): Connection/Socket/TCP, thread, buffer phía client & server.
     *      Sau vài giờ hệ thống sẽ đạt giới hạn (max connections / file descriptors), gây treo hoặc chậm dần.
     *   2) TĂNG TẢI DB & NETWORK: DB phải giữ session/transaction context; network giữ socket half-open.
     *      Dẫn đến lỗi "Communications link failure" (đứt kết nối/keepalive timeout/DB reset).
     *   3) ẢNH HƯỞNG HỆ THỐNG 24/7: bệnh viện cần realtime (cấp cứu, kê đơn, xét nghiệm).
     *      Treo phần mềm có thể làm gián đoạn quy trình, chậm xử lý, sai lệch dữ liệu.
     *   4) KHÓ TRIỆU CHỨNG: lỗi thường xuất hiện muộn (sau vài tiếng), khó debug, ảnh hưởng diện rộng.
     *
     * - Giải pháp: quản lý tập trung cấu hình, đảm bảo đóng tài nguyên ở finally (hoặc try-with-resources),
     *   ưu tiên dùng connection pool (HikariCP) trong dự án thực tế.
     */

    /**
     * PHẦN 2 - THỰC THI:
     */
    public static void demoQuery() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            st = conn.createStatement();

            // Query mẫu để test kết nối. Có thể thay bằng bảng thật.
            rs = st.executeQuery("SELECT 1 AS ok");

            while (rs.next()) {
                System.out.println("DB ok = " + rs.getInt("ok"));
            }

        } catch (SQLException e) {
            System.err.println("DB error: " + e.getMessage());
        } finally {
            // Đóng theo thứ tự ngược lại lúc mở
            DBContext.closeQuietly(rs);
            DBContext.closeQuietly(st);
            DBContext.closeQuietly(conn);
        }
    }

    public static void main(String[] args) {
        demoQuery();
    }
}


