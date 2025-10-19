package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SuaNhanSu {
    @FXML
    private TextField suanhansu_txma;
    @FXML
    private TextField suanhansu_txten;
    @FXML
    private ComboBox<String> suanhansu_cbgioitinh;
    @FXML
    private DatePicker suanhansu_datengaysinh;
    @FXML
    private TextField suanhansu_txcccd;
    @FXML
    private TextField suanhansu_txemail;
    @FXML
    private TextField suanhansu_txsdt;
    @FXML
    private ComboBox<PhongBan> suanhansu_cbmaPB;
    @FXML
    private ComboBox<String> suanhansu_cbchucvu;
    @FXML
    private Button suanhansu_btsua;
    @FXML
    private Button suanhansu_bttrolai;
    
    private NhanSu ns;
    private String maPhongBanCu;
    
    public void setData(NhanSu ns) {
        this.ns = ns;
        this.maPhongBanCu = ns.getMaPhongBan();

        suanhansu_txma.setText(ns.getMaNV());
        suanhansu_txten.setText(ns.getHoTen());
        suanhansu_datengaysinh.setValue(ns.getNgaySinh());
        suanhansu_txcccd.setText(ns.getCccd());
        suanhansu_txemail.setText(ns.getEmail());
        suanhansu_txsdt.setText(ns.getSdt());

        // Cài đặt cho các ComboBox
        suanhansu_cbgioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));

        ObservableList<PhongBan> dsPhongBan = DataService.getInstance().getDsPhongBan();
        suanhansu_cbmaPB.setItems(dsPhongBan);
        
        suanhansu_cbmaPB.setCellFactory(param -> new ListCell<PhongBan>() {
            @Override
            protected void updateItem(PhongBan pb, boolean empty) {
                super.updateItem(pb, empty);
                if (empty || pb == null) {
                    setText(null);
                } else {
                    setText(pb.getMaPhong() + " - " + pb.getTenPhong());
                }
            }
        });

        suanhansu_cbmaPB.setButtonCell(new ListCell<PhongBan>() {
            @Override
            protected void updateItem(PhongBan pb, boolean empty) {
                super.updateItem(pb, empty);
                if (empty || pb == null) {
                    setText(null);
                } else {
                    setText(pb.getMaPhong() + " - " + pb.getTenPhong());
                }
            }
        });

        // Đặt giá trị hiện tại
        suanhansu_cbgioitinh.setValue(ns.getGioiTinh());
        suanhansu_cbmaPB.setValue(DataService.getInstance().timPhongBanTheoMa(ns.getMaPhongBan()));
        suanhansu_cbchucvu.setValue(ns.getChucVu());
        
        suanhansu_txma.setDisable(true);
    }
    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("\\d+");
    }
    
    @FXML
    private void suanhansu_suaAction() throws IOException {
        String hoTen = suanhansu_txten.getText().trim();
        String gioiTinh = suanhansu_cbgioitinh.getValue();
        LocalDate ngaySinh = suanhansu_datengaysinh.getValue();
        String cccd = suanhansu_txcccd.getText().trim();
        String email = suanhansu_txemail.getText().trim();
        String sdt = suanhansu_txsdt.getText().trim();
        PhongBan selectedPB = suanhansu_cbmaPB.getValue();
        if (selectedPB == null) { // Kiểm tra nếu chưa chọn
            canhbao.canhbao("Thiếu thông tin", "Vui lòng chọn Phòng ban.");
            return;
        }
        String maPhongBanMoi = selectedPB.getMaPhong();
        
        String chucVu = suanhansu_cbchucvu.getValue();

        if (hoTen.isEmpty() || ngaySinh == null || maPhongBanMoi == null) {
            canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập Họ tên, Ngày sinh và chọn Phòng ban.");
            return; // Dừng lại nếu thiếu
        }

        // Kiểm tra CCCD (nếu người dùng có nhập)
        if (!cccd.isEmpty()) {
            if (!isNumeric(cccd) || cccd.length() != 12) {
                canhbao.canhbao("Sai định dạng", "CCCD (nếu nhập) phải là 12 ký tự số.");
                return;
            }
        }
    
        // Kiểm tra SĐT (nếu người dùng có nhập)
        if (!sdt.isEmpty()) {
            if (!isNumeric(sdt) || sdt.length() != 10) {
                canhbao.canhbao("Sai định dạng", "Số điện thoại (nếu nhập) phải là 10 ký tự số.");
                return;
            }
        }

        // Cập nhật DB
        ns.setHoTen(hoTen);
        ns.setGioiTinh(gioiTinh);
        ns.setNgaySinh(ngaySinh);
        ns.setCccd(cccd);
        ns.setEmail(email);
        ns.setSdt(sdt);
        ns.setMaPhongBan(maPhongBanMoi);
        ns.setChucVu(chucVu);
        
        DataService.getInstance().updateNhanSu(ns, this.maPhongBanCu);
        canhbao.thongbao("Thành công", "Cập nhật thông tin nhân sự thành công! Nhấn OK để thoát");
        App.setRoot("nhansu");
        
    }
    
    @FXML
    private void suanhansu_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("nhansu");
    }
}
