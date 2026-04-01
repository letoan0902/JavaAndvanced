package org.example.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * HashUtil - Tiện ích mã hóa mật khẩu
 *
 * Áp dụng:
 * - Bảo mật: Mã hóa mật khẩu bằng SHA-256 trước khi lưu vào DB
 * - Không lưu mật khẩu dạng plain text
 */
public class HashUtil {

    /**
     * Mã hóa mật khẩu bằng SHA-256
     *
     * @param password Mật khẩu gốc (plain text)
     * @return Chuỗi hex đã hash (64 ký tự)
     */
    public static String hashPassword(String password) {
        try {
            // Khởi tạo MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Hash mật khẩu thành mảng byte
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Chuyển mảng byte sang chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi: Không tìm thấy thuật toán SHA-256!", e);
        }
    }

    /**
     * So sánh mật khẩu nhập vào với mật khẩu đã hash trong DB
     *
     * @param rawPassword    Mật khẩu người dùng nhập
     * @param hashedPassword Mật khẩu đã hash từ DB
     * @return true nếu khớp, false nếu không
     */
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        String hashedInput = hashPassword(rawPassword);
        return hashedInput.equals(hashedPassword);
    }
}
