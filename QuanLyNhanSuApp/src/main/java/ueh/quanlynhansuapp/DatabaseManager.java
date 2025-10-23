package ueh.quanlynhansuapp;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp duy nhất chịu trách nhiệm cho mọi tương tác với cơ sở dữ liệu MySQL.
 */
public class DatabaseManager {

    // =================================================================
    // CÁC PHƯƠNG THỨC LIÊN QUAN ĐẾN PHÒNG BAN
    // =================================================================

    public List<PhongBan> loadAllPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        // Câu SQL này dùng LEFT JOIN và GROUP BY để đếm số nhân viên trong mỗi phòng
        String sql = "SELECT p.maPhong, p.tenPhong, p.maTruongPhong, p.sdtPhong, p.emailPhong, COUNT(n.maNV) as tongSoNhanVien " +
                     "FROM phongban p " +
                     "LEFT JOIN nhansu n ON p.maPhong = n.maPhongBan " +
                     "GROUP BY p.maPhong, p.tenPhong, p.maTruongPhong, p.sdtPhong, p.emailPhong";

        try (Connection conn = DatabaseConnection.getConnection();
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
            // In lỗi ra console để debug
            
        }
        return list;
    }

    public boolean addPhongBan(PhongBan pb) {
        String sql = "INSERT INTO phongban(maPhong, tenPhong, maTruongPhong, sdtPhong, emailPhong) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pb.getMaPhong());
            pstmt.setString(2, pb.getTenPhong());
            pstmt.setString(3, pb.getMaTruongPhong());
            pstmt.setString(4, pb.getSdtPhong());
            pstmt.setString(5, pb.getEmailPhong());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updatePhongBan(PhongBan pb) {
        String sql = "UPDATE phongban SET tenPhong = ?, maTruongPhong = ?, sdtPhong = ?, emailPhong = ? WHERE maPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pb.getTenPhong());
            pstmt.setString(2, pb.getMaTruongPhong());
            pstmt.setString(3, pb.getSdtPhong());
            pstmt.setString(4, pb.getEmailPhong());
            pstmt.setString(5, pb.getMaPhong());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Xóa phòng ban và chuyển nhân viên sang phòng 'P00' trong cùng một transaction.
     * Điều này đảm bảo toàn vẹn dữ liệu: hoặc cả hai thành công, hoặc cả hai thất bại.
     */
    public boolean deletePhongBanAndReassignNhanSu(String maPhong) {
        String updateSql = "UPDATE nhansu SET maPhongBan = 'P00' WHERE maPhongBan = ?";
        String deleteSql = "DELETE FROM phongban WHERE maPhong = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            // Bắt đầu transaction
            conn.setAutoCommit(false);

            // 1. Cập nhật nhân viên
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                pstmtUpdate.setString(1, maPhong);
                pstmtUpdate.executeUpdate();
            }

            // 2. Xóa phòng ban
            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                pstmtDelete.setString(1, maPhong);
                pstmtDelete.executeUpdate();
            }

            // Nếu không có lỗi, xác nhận transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Nếu có lỗi, hủy bỏ tất cả thay đổi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    // =================================================================
    // CÁC PHƯƠNG THỨC LIÊN QUAN ĐẾN NHÂN SỰ
    // =================================================================

    public List<NhanSu> loadAllNhanSu() {
        List<NhanSu> list = new ArrayList<>();
        String sql = "SELECT * FROM nhansu";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Date dbDate = rs.getDate("ngaySinh");
                LocalDate ngaySinh = (dbDate != null) ? dbDate.toLocalDate() : null;

                list.add(new NhanSu(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getString("gioiTinh"),
                        ngaySinh,
                        rs.getString("cccd"),
                        rs.getString("email"),
                        rs.getString("sdt"),
                        rs.getString("maPhongBan"),
                        rs.getString("chucVu")
                ));
            }
        } catch (SQLException e) {
        }
        return list;
    }

    public boolean addNhanSu(NhanSu ns) {
        String sql = "INSERT INTO nhansu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ns.getMaNV());
            pstmt.setString(2, ns.getHoTen());
            pstmt.setString(3, ns.getGioiTinh());
            pstmt.setDate(4, ns.getNgaySinh() != null ? Date.valueOf(ns.getNgaySinh()) : null);
            pstmt.setString(5, ns.getCccd());
            pstmt.setString(6, ns.getEmail());
            pstmt.setString(7, ns.getSdt());
            pstmt.setString(8, ns.getMaPhongBan());
            pstmt.setString(9, ns.getChucVu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNhanSu(NhanSu ns) {
        String sql = "UPDATE nhansu SET hoTen = ?, gioiTinh = ?, ngaySinh = ?, cccd = ?, email = ?, sdt = ?, maPhongBan = ?, chucVu = ? WHERE maNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ns.getHoTen());
            pstmt.setString(2, ns.getGioiTinh());
            pstmt.setDate(3, ns.getNgaySinh() != null ? Date.valueOf(ns.getNgaySinh()) : null);
            pstmt.setString(4, ns.getCccd());
            pstmt.setString(5, ns.getEmail());
            pstmt.setString(6, ns.getSdt());
            pstmt.setString(7, ns.getMaPhongBan());
            pstmt.setString(8, ns.getChucVu());
            pstmt.setString(9, ns.getMaNV());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteNhanSu(String maNV) {
        String sql = "DELETE FROM nhansu WHERE maNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}