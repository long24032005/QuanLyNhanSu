package ueh.quanlynhansuapp;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
Lớp này chịu trách nhiệm cho mọi thao tác với cơ sở dữ liệu MySQL:
 - Lấy dữ liệu (SELECT)
 - Thêm, xóa, sửa (INSERT, UPDATE, DELETE)
 - DatabaseManager được gọi gián tiếp thông qua lớp DataService.
 */
public class DatabaseManager {

    
    // Các hàm  liên quan đến phòng ban
    
    // Lấy toàn bộ danh sách phòng ban từ database
    public List<PhongBan> loadAllPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT p.maPhong, p.tenPhong, p.maTruongPhong, p.sdtPhong, p.emailPhong, COUNT(n.maNV) as tongSoNhanVien " +  // COUNT(n.maNV) đếm tổng nhân viên của mỗi phòng
                     "FROM phongban p " +
                     "LEFT JOIN nhansu n ON p.maPhong = n.maPhongBan " +  // Dùng LEFT JOIN để lấy cả những phòng chưa có nhân viên
                     "GROUP BY p.maPhong, p.tenPhong, p.maTruongPhong, p.sdtPhong, p.emailPhong";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Duyệt từng dòng trong kết quả truy vấn   
            while (rs.next()) {
                list.add(new PhongBan(
                        rs.getString("maPhong"),
                        rs.getString("tenPhong"),
                        rs.getString("maTruongPhong"),
                        rs.getString("sdtPhong"),
                        rs.getString("emailPhong"),
                        rs.getInt("tongSoNhanVien")  // Lấy số lượng nhân viên đã đếm được
                ));
            }
        } catch (SQLException e) {
            // In lỗi ra console để debug
            System.err.println("Lỗi khi tải danh sách phòng ban: " + e.getMessage());
        }
        return list;
    }

    // Thêm một phòng ban mới vào database 
    public boolean addPhongBan(PhongBan pb) {
        String sql = "INSERT INTO phongban(maPhong, tenPhong, maTruongPhong, sdtPhong, emailPhong) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Gán giá trị lần lượt cho các dấu hỏi (?) trong câu SQL
            pstmt.setString(1, pb.getMaPhong());
            pstmt.setString(2, pb.getTenPhong());
            pstmt.setString(3, pb.getMaTruongPhong());
            pstmt.setString(4, pb.getSdtPhong());
            pstmt.setString(5, pb.getEmailPhong());
            return pstmt.executeUpdate() > 0; // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng (tức là thêm thành công)
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm phòng ban: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật thông tin của một phòng ban đã tồn tại trong database
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

    /*
    Xóa một phòng ban ra khỏi hệ thống. Trước khi xóa, toàn bộ nhân viên của phòng đó sẽ được chuyển sang phòng "P00".
    Dùng transaction để đảm bảo:
     - Nếu cả hai bước đều thành công => lưu lại.
     - Nếu một trong hai bước lỗi => quay lại trạng thái ban đầu.
    */
    public boolean deletePhongBanAndReassignNhanSu(String maPhong) {
        String updateSql = "UPDATE nhansu SET maPhongBan = 'P00' WHERE maPhongBan = ?";
        String deleteSql = "DELETE FROM phongban WHERE maPhong = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Tắt auto-commit để bắt đầu transaction

            // Cập nhật nhân viên về phòng P00
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                pstmtUpdate.setString(1, maPhong);
                pstmtUpdate.executeUpdate();
            }

            // Xóa phòng ban
            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                pstmtDelete.setString(1, maPhong);
                pstmtDelete.executeUpdate();
            }

            // Nếu không có lỗi => xác nhận transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa phòng ban: " + e.getMessage());
            if (conn != null) {  // Nếu có lỗi, hủy bỏ tất cả thay đổi
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {   // Luôn bật lại chế độ autoCommit và đóng kết nối
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    
    // Các hàm  liên quan đến nhân sự
    
    // Lấy toàn bộ danh sách nhân sự từ database
    public List<NhanSu> loadAllNhanSu() {
        List<NhanSu> list = new ArrayList<>();
        String sql = "SELECT * FROM nhansu";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                 // Chuyển kiểu java.sql.Date sang LocalDate để dùng trong JavaFX
                Date dbDate = rs.getDate("ngaySinh"); 
                LocalDate ngaySinh = (dbDate != null) ? dbDate.toLocalDate() : null;

                NhanSu ns = new NhanSu(
                    rs.getString("maNV"),
                    rs.getString("hoTen"),
                    rs.getString("gioiTinh"),
                    ngaySinh,
                    rs.getString("cccd"),
                    rs.getString("email"),
                    rs.getString("sdt"),
                    rs.getString("maPhongBan"),
                    rs.getString("chucVu")
                );
                ns.setMatKhau(rs.getString("matkhau")); // lấy mật khẩu
                list.add(ns);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi load danh sách nhân sự: " + e.getMessage());
        }

        return list;
    }
    
 
    // Thêm nhân viên mới vào bảng nhansu. Nếu không nhập mật khẩu => tự đặt mặc định là "123456".
    public boolean addNhanSu(NhanSu ns) {
        String sql = "INSERT INTO nhansu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu, matkhau) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pstmt.setString(10, ns.getMatKhau() != null ? ns.getMatKhau() : "123456");
            
            return pstmt.executeUpdate() > 0;
        
        } catch (SQLException e) {
           System.err.println("Lỗi khi thêm nhân sự: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật thông tin của một nhân viên đã có sẵn 
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

     // Xóa nhân viên khỏi hệ thống theo mã nhân viên 
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
    
    
    // Các hàm liên quan đến lương thưởng
    
    // Lấy toàn bộ dữ liệu từ bảng luongthuong
    public List<LuongThuong> loadAllLuongThuong() {
        List<LuongThuong> list = new ArrayList<>();
        String sql = "SELECT * FROM luongthuong";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new LuongThuong(
                        rs.getString("maLuong"),
                        rs.getString("maNV"),          // ✅ Sửa lại đúng tên cột trong DB
                        rs.getString("thangNam"),
                        rs.getDouble("luongCoBan"),
                        rs.getDouble("phuCap"),
                        rs.getDouble("thuong"),
                        rs.getDouble("khauTru"),
                        rs.getString("ngayChiTra")
                ));
            }

            System.out.println("Đã load " + list.size() + " bản ghi từ bảng luongthuong.");

        } catch (SQLException e) {
             System.err.println("Lỗi khi tải bảng lương thưởng: " + e.getMessage());
        }

        return list;
    }

    // Thêm bản ghi lương thưởng mới
    public boolean addLuongThuong(LuongThuong lt) {
        // ✅ Đổi tên cột maNhanVien -> maNV cho khớp với DB
        String sql = "INSERT INTO luongthuong(maLuong, maNV, thangNam, luongCoBan, phuCap, thuong, khauTru, ngayChiTra) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lt.getMaLuong());
            pstmt.setString(2, lt.getMaNhanVien());
            pstmt.setString(3, lt.getThangNam());
            pstmt.setDouble(4, lt.getLuongCoBan());
            pstmt.setDouble(5, lt.getPhuCap());
            pstmt.setDouble(6, lt.getThuong());
            pstmt.setDouble(7, lt.getKhauTru());
            pstmt.setString(8, lt.getNgayChiTra());

            int rows = pstmt.executeUpdate();
            System.out.println("Thêm lương thưởng: " + rows + " dòng bị ảnh hưởng.");
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm lương thưởng: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật thông tin lương thưởng    
    public boolean updateLuongThuong(LuongThuong lt) {
        String sql = "UPDATE luongthuong "
                   + "SET maNV = ?, thangNam = ?, luongCoBan = ?, phuCap = ?, thuong = ?, khauTru = ?, ngayChiTra = ? "
                   + "WHERE maLuong = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lt.getMaNhanVien());
            pstmt.setString(2, lt.getThangNam());
            pstmt.setDouble(3, lt.getLuongCoBan());
            pstmt.setDouble(4, lt.getPhuCap());
            pstmt.setDouble(5, lt.getThuong());
            pstmt.setDouble(6, lt.getKhauTru());
            pstmt.setString(7, lt.getNgayChiTra());
            pstmt.setString(8, lt.getMaLuong());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật lương thưởng: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa bản ghi lương thưởng theo mã lương
    public boolean deleteLuongThuong(String maLuong) {
        String sql = "DELETE FROM luongthuong WHERE maLuong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maLuong);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa lương thưởng: " + e.getMessage());
            return false;
        }
    }

}