package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
public class SuaPhongBan {
    @FXML
    TextField suaphongban_txma;
    @FXML
    TextField suaphongban_txten;
    @FXML
    TextField suaphongban_txmaTP;
    @FXML
    TextField suaphongban_txsdt;
    @FXML
    TextField suaphongban_txemail;
    @FXML
    TextField suaphongban_txtong;
    @FXML
    Button suaphongban_btsua;
    @FXML
    Button suaphongban_bttrolai;
    private PhongBan pBan;
    
    
    public void setData(PhongBan pBan) {
        this.pBan = pBan;
        suaphongban_txma.setText(pBan.getMaPhong());
        suaphongban_txten.setText(pBan.getTenPhong());
        suaphongban_txmaTP.setText(pBan.getMaTruongPhong());
        suaphongban_txsdt.setText(pBan.getSdtPhong());
        suaphongban_txemail.setText(pBan.getEmailPhong());
        suaphongban_txtong.setText(String.valueOf(pBan.getTongSoNhanVien()));
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
            pBan.setMaPhong(ma);
            pBan.setTenPhong(ten);
            pBan.setMaTruongPhong(maTP);
            pBan.setSdtPhong(sdt);
            pBan.setEmailPhong(email);
            pBan.setTongSoNhanVien(tongNV);

            // Gọi Database để cập nhật 
            Database.updatePhongBan(pBan);

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