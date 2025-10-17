package ueh.quanlynhansuapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class SuaNhanSu {
    @FXML
    private TextField suanhansu_txma;
    @FXML
    private TextField suanhansu_txten;
    @FXML
    private ChoiceBox suanhansu_cbgioitinh;
    @FXML
    private DatePicker suanhansu_datengaysinh;
    @FXML
    private TextField suanhansu_txcccd;
    @FXML
    private TextField suanhansu_txemail;
    @FXML
    private TextField suanhansu_txsdt;
    @FXML
    private ChoiceBox suanhansu_cbmaPB;
    @FXML
    private ChoiceBox suanhansu_cbchucvu;
    @FXML
    private Button suanhansu_btsua;
    @FXML
    private Button suanhansu_bttrolai;
    
    private NhanSu ns;
    
    public void setData(NhanSu ns) {
        this.ns = ns;
        suanhansu_txma.setText(ns.getMaNV());
        suanhansu_txten.setText(ns.getHoTen());
        suanhansu_cbgioitinh.setValue(ns.getGioiTinh());
        suanhansu_datengaysinh.setValue(ns.getNgaySinh());
        suanhansu_txcccd.setText(ns.getCccd());
        suanhansu_txemail.setText(ns.getEmail());
        suanhansu_txsdt.setText(ns.getSdt());
        suanhansu_cbmaPB.setValue(ns.getMaPhongBan());
        suanhansu_cbchucvu.setValue(ns.getChucVu());
    }
    
    @FXML
    private void suanhansu_suaAction() throws IOException {
        String ma = suanhansu_txma.getText().trim();
        String ten = suanhansu_txten.getText().trim();
        String gioitinh = (String) suanhansu_cbgioitinh.getValue();
        String cccd = suanhansu_txcccd.getText().trim();
        String email = suanhansu_txemail.getText().trim();
        String sdt = suanhansu_txsdt.getText().trim();
        String maPB = (String) suanhansu_cbmaPB.getValue();
        String chucvu = (String) suanhansu_cbchucvu.getValue();

        if (ma.isEmpty() || ten.isEmpty() || gioitinh == null || cccd.isEmpty() || email.isEmpty() || sdt.isEmpty()
                || maPB == null || chucvu == null || suanhansu_datengaysinh.getValue() == null) {
            canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập đầy đủ tất cả các trường.");
            return;
        }

        // Cập nhật DB
        try (Connection conn = Database.getConnection()) {
            if (conn == null) {
                canhbao.canhbao("Lỗi", "Không thể kết nối cơ sở dữ liệu.");
                return;
            }

            String sql = "UPDATE nhansu SET ten=?, gioitinh=?, ngaysinh=?, cccd=?, email=?, sdt=?, maPB=?, chucvu=? WHERE ma=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ten);
            ps.setString(2, gioitinh);
            ps.setDate(3, java.sql.Date.valueOf(suanhansu_datengaysinh.getValue()));
            ps.setString(4, cccd);
            ps.setString(5, email);
            ps.setString(6, sdt);
            ps.setString(7, maPB);
            ps.setString(8, chucvu);
            ps.setString(9, ma);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                canhbao.thongbao("Thành công", "Cập nhật thông tin nhân sự thành công!");
                App.setRoot("nhansu");
            } else {
                canhbao.canhbao("Thất bại", "Không tìm thấy nhân sự để cập nhật!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            canhbao.canhbao("Lỗi SQL", "Không thể cập nhật dữ liệu: " + e.getMessage());
        }
    }
    
    @FXML
    private void suanhansu_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("nhansu");
    }
}
