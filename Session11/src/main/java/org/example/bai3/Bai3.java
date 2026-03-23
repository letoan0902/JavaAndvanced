package org.example.bai3;

import org.example.bai1.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bai3 {
    /*
     * PHẦN 1 - PHÂN TÍCH:
     *
     * - executeUpdate() trả về số DÒNG (rows) bị tác động bởi câu lệnh DML
     *   (INSERT / UPDATE / DELETE).
     *   Ví dụ:
     *   + Trả về 1: có 1 dòng được update thành công.
     *   + Trả về 0: không có dòng nào bị thay đổi (thường do WHERE không match bản ghi nào).
     *
     * - Ứng dụng vào bài:
     *   Khi cập nhật theo bed_id, nếu executeUpdate() == 0 => bed_id không tồn tại
     *   (hoặc dữ liệu đã ở trạng thái đó và DB không tính là thay đổi tùy hệ quản trị).
     *   Ta dùng giá trị này để phản hồi chính xác cho y tá: "Mã giường này không tồn tại".
     */

    /*
     * PHẦN 2 - THỰC THI:
     */
    public static void updateBedStatusToOccupied(String bedId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String sql = "UPDATE Beds SET bed_status = ? WHERE bed_id = ?";

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Occupied");
            ps.setString(2, bedId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Lỗi: Không tìm thấy mã giường: " + bedId);
            } else {
                System.out.println("Đã cập nhật giường '" + bedId + "' sang trạng thái Đang sử dụng.");
            }

        } catch (SQLException e) {
            System.err.println("DB error while updating bed status: " + e.getMessage());
        } finally {
            DBContext.closeQuietly(ps);
            DBContext.closeQuietly(conn);
        }
    }

    public static void main(String[] args) {
        String bedId = (args != null && args.length > 0) ? args[0] : "Bed_999";
        updateBedStatusToOccupied(bedId);
    }
}



