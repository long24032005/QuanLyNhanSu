package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class SuaPhongBan {
    @FXML
    Textfield suaphongban_txma;
    @FXML
    Textfield suaphongban_txten;
    @FXML
    Textfield suaphongban_txmaTP;
    @FXML
    Textfield suaphongban_txsdt;
    @FXML
    Textfield suaphongban_txemail;
    @FXML
    Textfield suaphongban_txtong;
    @FXML
    Button suaphongban_btsua;
    @FXML
    Button suaphongban_bttrolai;
    
    
    public void setData(PhongBan phongBan) {
        this.phongBan = pb;
        suaphongban_txma.setText(pb.getMaPhong());
        suaphongban_txten.setText(pb.getTenPhong());
        suaphongban_txmaTP.setText(pb.getMaTP());
        suaphongban_txsdt.setText(pb.getSdt());
        suaphongban_txemail.setText(pb.getEmail());
        suaphongban_txtong.setText(String.valueOf(pb.getTongNhanVien()));
    }
    
     @FXML
    private void suaphongban_suaAction() throws IOException {
        // Lấy dữ liệu mới
        String ma = suaphongban_txma.getText().trim();
        String ten = suaphongban_txten.getText().trim();
        String maTP = suaphongban_txmaTP.getText().trim();
        String sdt = suaphongban_txsdt.getText().trim();
        String email = suaphongban_txemail.getText().trim();
        String tong = suaphongban_txtong.getText().trim();

        // Kiểm tra dữ liệu trống
        if (ma.isEmpty() || ten.isEmpty() || maTP.isEmpty() || sdt.isEmpty() || email.isEmpty() || tong.isEmpty()) {
            canhbao.canhbao("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            int tongNV = Integer.parseInt(tong);

            // Cập nhật lại thông tin phòng ban
            phongBan.setMaPhong(ma);
            phongBan.setTenPhong(ten);
            phongBan.setMaTP(maTP);
            phongBan.setSdt(sdt);
            phongBan.setEmail(email);
            phongBan.setTongNhanVien(tongNV);

            // Gọi Database để cập nhật 
            Database.updatePhongBan(phongBan);

            // Thông báo thành công
            canhbao.thongbao("Thành công", "Đã cập nhật thông tin phòng ban!");

            // Quay lại giao diện chính
            App.setRoot("phongban");

        } catch (NumberFormatException e) {
            canhbao.canhbao("Lỗi", "Tổng nhân viên phải là số!");
        }
    }
    
    
    @FXML
    private void suaphongban_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("phongban");
    }
}