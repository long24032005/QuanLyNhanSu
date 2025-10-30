package ueh.quanlynhansuapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Lớp này tạo kết nối đến cơ sở dữ liệu MySQL.
 - Dùng DriverManager để mở kết nối theo thông tin cấu hình.
 - Được gọi ở các lớp khác khi cần truy vấn hoặc cập nhật dữ liệu.
 */
public class DatabaseConnection {
    // Đường dẫn kết nối đến MySQL (tên database: quanlynhansu_db)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quanlynhansu_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    // Tên đăng nhập và mật khẩu của MySQL
    private static final String USER = "root";
    private static final String PASS = "Nam@2005"; 

    // Hàm mở kết nối tới database, 
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS); // Tạo kết nối đến MySQL bằng thông tin URL, USER, PASS
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());    // Nếu lỗi (ví dụ: sai mật khẩu, MySQL chưa mở, sai tên DB)
            return null;
        }
    }
}