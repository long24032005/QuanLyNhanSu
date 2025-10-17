package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.stream.Collectors;

public class SuaPhongBan {
    @FXML
    private TextField suaphongban_txma;
    @FXML
    private TextField suaphongban_txten;
    @FXML
    private ComboBox<String> suaphongban_cbmaTP;
    @FXML
    private TextField suaphongban_txsdt;
    @FXML
    private TextField suaphongban_txemail;
    @FXML
    private TextField suaphongban_txtong;
    @FXML
    private Button suaphongban_btsua;
    @FXML
    private Button suaphongban_bttrolai;
    
    private PhongBan pBan;
    
    
    public void setData(PhongBan pBan) {
        this.pBan = pBan;
        suaphongban_txma.setText(pBan.getMaPhong());
        suaphongban_txten.setText(pBan.getTenPhong());
        suaphongban_txsdt.setText(pBan.getSdtPhong());
        suaphongban_txemail.setText(pBan.getEmailPhong());
        suaphongban_txtong.setText(String.valueOf(pBan.getTongSoNhanVien()));
        
        // Lấy danh sách nhân viên CHỈ thuộc phòng ban này
        ObservableList<String> nhanVienTrongPhong = DataService.getInstance().getDsNhanSu()
                .stream() // Bắt đầu xử lý luồng dữ liệu
                .filter(ns -> ns.getMaPhongBan().equals(pBan.getMaPhong())) // Lọc ra những ai có mã phòng ban trùng khớp
                .map(NhanSu::getMaNV) // Chỉ lấy mã nhân viên của họ
                .collect(Collectors.toCollection(FXCollections::observableArrayList)); // Thu thập kết quả vào một danh sách mới

        // Thêm một lựa chọn "Bỏ trống" để có thể không chọn ai làm trưởng phòng
        nhanVienTrongPhong.add(0, " (Bỏ trống)");
        
        // Gán danh sách này cho ComboBox
        suaphongban_cbmaTP.setItems(nhanVienTrongPhong);
        
        // Chọn sẵn trưởng phòng hiện tại (nếu có)
        if (pBan.getMaTruongPhong() != null && !pBan.getMaTruongPhong().isEmpty()) {
            suaphongban_cbmaTP.setValue(pBan.getMaTruongPhong());
        } else {
            suaphongban_cbmaTP.getSelectionModel().selectFirst(); // Chọn "Bỏ trống"
        }

        suaphongban_txma.setDisable(true);
        suaphongban_txtong.setDisable(true); 
    }
    
    @FXML
    private void suaphongban_suaAction() throws IOException {
        String ten = suaphongban_txten.getText().trim();
        String maTP = suaphongban_cbmaTP.getValue();
        // Xử lý trường hợp người dùng chọn "Bỏ trống"
        if (maTP != null && maTP.contains("Bỏ trống")) {
            maTP = null; // Lưu giá trị null hoặc rỗng vào file Excel
        }
        
        String sdt = suaphongban_txsdt.getText().trim();
        String email = suaphongban_txemail.getText().trim();

        if (ten.isEmpty()) {
            canhbao.canhbao("Lỗi", "Tên phòng ban không được để trống!");
            return;
        }

        pBan.setTenPhong(ten);
        pBan.setMaTruongPhong(maTP); // Cập nhật mã trưởng phòng mới
        pBan.setSdtPhong(sdt);
        pBan.setEmailPhong(email);
        
        DataService.getInstance().updatePhongBan(pBan);

        canhbao.thongbao("Thành công", "Đã cập nhật thông tin phòng ban!");
        App.setRoot("phongban");
    }
    
    @FXML
    private void suaphongban_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("phongban");
    }
}