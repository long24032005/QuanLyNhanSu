/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

/**
 *
 * @author philo
 */
public class Database {
// Đường dẫn tới file database. Sẽ tự động tạo trong thư mục gốc của dự án nếu chưa có.
    private static final String URL = "jdbc:sqlite:quanlynhansu.db";

    /**
     * Lấy kết nối tới SQLite database.
     * @return Đối tượng Connection hoặc null nếu có lỗi.
     */
    static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Không thể kết nối tới cơ sở dữ liệu: " + e.getMessage());
            return null;
        }
    }

    /**
     * Khởi tạo database, tạo các bảng cần thiết nếu chúng chưa tồn tại.
     */
    public static void initializeDatabase() {
        String sqlPhongBan = "CREATE TABLE IF NOT EXISTS phongban (" +
                             "maPhong TEXT PRIMARY KEY," +
                             "tenPhong TEXT NOT NULL," +
                             "maTruongPhong TEXT," +
                             "sdtPhong TEXT," +
                             "emailPhong TEXT," +
                             "tongSoNhanVien INTEGER NOT NULL DEFAULT 0" +
                             ");";

        String sqlNhanSu = "CREATE TABLE IF NOT EXISTS nhansu (" +
                           "maNV TEXT PRIMARY KEY," +
                           "hoTen TEXT NOT NULL," +
                           "gioiTinh TEXT," +
                           "ngaySinh TEXT NOT NULL," + // SQLite lưu ngày tháng dưới dạng TEXT "YYYY-MM-DD"
                           "cccd TEXT UNIQUE," +
                           "email TEXT," +
                           "sdt TEXT," +
                           "maPhongBan TEXT NOT NULL," +
                           "chucVu TEXT," +
                           "FOREIGN KEY (maPhongBan) REFERENCES phongban(maPhong) ON UPDATE CASCADE ON DELETE SET DEFAULT" +
                           ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sqlPhongBan);
                stmt.execute(sqlNhanSu);
                // Thêm phòng "Chờ phân công" làm phòng mặc định nếu chưa tồn tại
                stmt.execute("INSERT OR IGNORE INTO phongban(maPhong, tenPhong, tongSoNhanVien) VALUES('P00', 'Chờ phân công', 0);");
            }
        } catch (SQLException e) {
             canhbao.canhbao("Lỗi Database", "Không thể khởi tạo bảng: " + e.getMessage());
        }
    }

    // ================== CÁC PHƯƠNG THỨC CHO PHÒNG BAN ==================

    public static List<PhongBan> loadAllPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM phongban";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new PhongBan(
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("maTruongPhong"),
                    rs.getString("sdtPhong"),
                    rs.getString("emailPhong"),
                    rs.getInt("tongSoNhanVien")
                ));
            }
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Không thể tải danh sách phòng ban: " + e.getMessage());
        }
        return list;
    }

    public static boolean insertPhongBan(PhongBan pb) {
        String sql = "INSERT INTO phongban(maPhong, tenPhong, maTruongPhong, sdtPhong, emailPhong, tongSoNhanVien) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pb.getMaPhong());
            pstmt.setString(2, pb.getTenPhong());
            pstmt.setString(3, pb.getMaTruongPhong());
            pstmt.setString(4, pb.getSdtPhong());
            pstmt.setString(5, pb.getEmailPhong());
            pstmt.setInt(6, pb.getTongSoNhanVien());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Thêm phòng ban thất bại: " + e.getMessage());
            return false;
        }
    }

    public static void updatePhongBan(PhongBan pBan) {
         String sql = "UPDATE phongban SET tenPhong = ?, maTruongPhong = ?, sdtPhong = ?, emailPhong = ?, tongSoNhanVien = ? WHERE maPhong = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pBan.getTenPhong());
            pstmt.setString(2, pBan.getMaTruongPhong());
            pstmt.setString(3, pBan.getSdtPhong());
            pstmt.setString(4, pBan.getEmailPhong());
            pstmt.setInt(5, pBan.getTongSoNhanVien());
            pstmt.setString(6, pBan.getMaPhong());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Cập nhật phòng ban thất bại: " + e.getMessage());
        }
    }

    public static boolean deletePhongBan(String maPhong) {
        String sql = "DELETE FROM phongban WHERE maPhong = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhong);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Xóa phòng ban thất bại: " + e.getMessage());
            return false;
        }
    }

    // ================== CÁC PHƯƠNG THỨC CHO NHÂN SỰ ==================

    public static List<NhanSu> loadAllNhanSu() {
        List<NhanSu> list = new ArrayList<>();
        String sql = "SELECT * FROM nhansu";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new NhanSu(
                    rs.getString("maNV"),
                    rs.getString("hoTen"),
                    rs.getString("gioiTinh"),
                    LocalDate.parse(rs.getString("ngaySinh")), // Chuyển từ text "YYYY-MM-DD" sang LocalDate
                    rs.getString("cccd"),
                    rs.getString("email"),
                    rs.getString("sdt"),
                    rs.getString("maPhongBan"),
                    rs.getString("chucVu")
                ));
            }
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Không thể tải danh sách nhân sự: " + e.getMessage());
        }
        return list;
    }

    public static boolean insertNhanSu(NhanSu ns) {
        String sql = "INSERT INTO nhansu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ns.getMaNV());
            pstmt.setString(2, ns.getHoTen());
            pstmt.setString(3, ns.getGioiTinh());
            pstmt.setString(4, ns.getNgaySinh().toString()); // Chuyển LocalDate thành text "YYYY-MM-DD"
            pstmt.setString(5, ns.getCccd());
            pstmt.setString(6, ns.getEmail());
            pstmt.setString(7, ns.getSdt());
            pstmt.setString(8, ns.getMaPhongBan());
            pstmt.setString(9, ns.getChucVu());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Thêm nhân sự thất bại: " + e.getMessage());
            return false;
        }
    }

     public static void updateNhanSu(NhanSu ns) {
        String sql = "UPDATE nhansu SET hoTen = ?, gioiTinh = ?, ngaySinh = ?, cccd = ?, email = ?, sdt = ?, maPhongBan = ?, chucVu = ? WHERE maNV = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ns.getHoTen());
            pstmt.setString(2, ns.getGioiTinh());
            pstmt.setString(3, ns.getNgaySinh().toString());
            pstmt.setString(4, ns.getCccd());
            pstmt.setString(5, ns.getEmail());
            pstmt.setString(6, ns.getSdt());
            pstmt.setString(7, ns.getMaPhongBan());
            pstmt.setString(8, ns.getChucVu());
            pstmt.setString(9, ns.getMaNV());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Cập nhật nhân sự thất bại: " + e.getMessage());
        }
    }

    public static boolean deleteNhanSu(String maNV) {
         String sql = "DELETE FROM nhansu WHERE maNV = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi Database", "Xóa nhân sự thất bại: " + e.getMessage());
            return false;
        }
    }
}
