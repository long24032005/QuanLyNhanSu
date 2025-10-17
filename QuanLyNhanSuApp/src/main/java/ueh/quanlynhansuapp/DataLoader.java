/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author lagia
 */
public class DataLoader {
    public static void loadInitialData() {
        // Sử dụng Connection được cung cấp bởi lớp Database
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                // Tải dữ liệu phòng ban từ file CSV
                loadData(conn, "phongban.csv", "INSERT OR IGNORE INTO phongban(maPhong, tenPhong, maTruongPhong, sdtPhong, emailPhong, tongSoNhanVien) VALUES(?,?,?,?,?,?)");
                // Tải dữ liệu nhân sự từ file CSV
                loadData(conn, "nhansu.csv", "INSERT OR IGNORE INTO nhansu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu) VALUES(?,?,?,?,?,?,?,?,?)");
            }
        } catch (SQLException e) {
            canhbao.canhbao("Lỗi SQL khi tải dữ liệu", "Không thể thực thi câu lệnh SQL: " + e.getMessage());
        }
    }

    private static void loadData(Connection conn, String fileName, String sql) {
        // Dùng try-with-resources để tự động đóng file sau khi đọc
        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            
            String line;
            br.readLine(); // Bỏ qua dòng tiêu đề đầu tiên

            // Chuẩn bị câu lệnh SQL
            PreparedStatement pstmt = conn.prepareStatement(sql);

            while ((line = br.readLine()) != null) {
                // Tách các giá trị bằng dấu phẩy
                // Tham số -1 để giữ lại các giá trị trống ở cuối dòng nếu có
                String[] values = line.split(",", -1); 

                // Gán từng giá trị vào câu lệnh PreparedStatement
                for (int i = 0; i < values.length; i++) {
                    // Nếu giá trị rỗng, set nó thành NULL trong SQL, ngược lại set giá trị bình thường
                    if (values[i] == null || values[i].isEmpty()) {
                        pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(i + 1, values[i]);
                    }
                }
                pstmt.executeUpdate(); // Thực thi câu lệnh chèn dữ liệu
            }
            
        } catch (IOException | SQLException e) {
            canhbao.canhbao("Lỗi đọc file hoặc DB", "Không thể đọc file " + fileName + " hoặc chèn dữ liệu: " + e.getMessage());
        }
    }
}
