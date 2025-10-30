package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class SuaNhanSu {
    @FXML private TextField suanhansu_txma;
    @FXML private TextField suanhansu_txten;
    @FXML private ComboBox<String> suanhansu_cbgioitinh;
    @FXML private DatePicker suanhansu_datengaysinh;
    @FXML private TextField suanhansu_txcccd;
    @FXML private TextField suanhansu_txemail;
    @FXML private TextField suanhansu_txsdt;
    @FXML private ComboBox<PhongBan> suanhansu_cbmaPB;
    @FXML private ComboBox<String> suanhansu_cbchucvu;
    @FXML private Button suanhansu_btsua;
    @FXML private Button suanhansu_bttrolai;
    
    private NhanSu ns;
    private String maPhongBanCu;
    private final DataService dataService = DataService.getInstance();
    
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
        String chucVu = suanhansu_cbchucvu.getValue();
    

        if (hoTen.isEmpty() || gioiTinh == null || gioiTinh.isEmpty() || ngaySinh == null || cccd.isEmpty() || email.isEmpty() || sdt.isEmpty()|| selectedPB == null || chucVu == null || chucVu.isEmpty()) {
            canhbao.canhbao("Thiếu thông tin",
                "Vui lòng điền đầy đủ tất cả thông tin!");
            return;
        }

        // --- Kiểm tra CCCD ---
        if (!cccd.isEmpty()) {
            if (!isNumeric(cccd)) {
                canhbao.canhbao("Sai định dạng", "CCCD chỉ được phép chứa các ký tự số (0–9).");
                return;
            }
            if (cccd.length() != 12) {
                canhbao.canhbao("Sai số lượng ký tự", "CCCD phải gồm đúng 12 chữ số.");
                return;
            }
            boolean cccdTrung = dataService.getDsNhanSu().stream()
                .anyMatch(p -> !p.getMaNV().equals(ns.getMaNV()) && cccd.equals(p.getCccd()));
            if (cccdTrung) {
                canhbao.canhbao("Trùng CCCD", "CCCD \"" + cccd + "\" đã tồn tại cho một nhân viên khác.");
                return;
            }
        }

        // --- Kiểm tra Email ---
        if (!email.isEmpty()) {
            boolean emailTrung = dataService.getDsNhanSu().stream()
                .anyMatch(p -> !p.getMaNV().equals(ns.getMaNV()) && email.equalsIgnoreCase(p.getEmail()));
            if (emailTrung) {
                canhbao.canhbao("Trùng Email", "Email \"" + email + "\" đã được sử dụng bởi nhân viên khác.");
                return;
            }
        }

        // --- Kiểm tra SĐT ---
        if (!sdt.isEmpty()) {
            if (!isNumeric(sdt)) {
                canhbao.canhbao("Sai định dạng", "SĐT chỉ được phép chứa các ký tự số (0–9).");
                return;
            }
            if (sdt.length() != 10) {
                canhbao.canhbao("Sai số lượng ký tự", "SĐT phải gồm đúng 10 chữ số.");
                return;
            }
            boolean sdtTrung = dataService.getDsNhanSu().stream()
                .anyMatch(p -> !p.getMaNV().equals(ns.getMaNV()) && sdt.equals(p.getSdt()));
            if (sdtTrung) {
                canhbao.canhbao("Trùng Số điện thoại", "SĐT \"" + sdt + "\" đã được dùng bởi nhân viên khác.");
                return;
            }
        }

        // --- Kiểm tra chức vụ ---
        if (chucVu == null || chucVu.isEmpty()) {
            canhbao.canhbao("Thiếu chức vụ", "Vui lòng chọn chức vụ cho nhân sự.");
            return;
        }

        // --- Cập nhật dữ liệu ---
        ns.setHoTen(hoTen);
        ns.setGioiTinh(gioiTinh);
        ns.setNgaySinh(ngaySinh);
        ns.setCccd(cccd);
        ns.setEmail(email);
        ns.setSdt(sdt);
        ns.setMaPhongBan(selectedPB.getMaPhong());
        ns.setChucVu(chucVu);

        dataService.updateNhanSu(ns);

        // --- Thông báo thành công & quay lại ---
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("nhansu.fxml"));
        javafx.scene.Parent root = loader.load();

        javafx.stage.Stage stage = (javafx.stage.Stage) suanhansu_btsua.getScene().getWindow();
        stage.getScene().setRoot(root);

        // --- Sau khi giao diện đã load xong, hiển thị thông báo ---
        javafx.application.Platform.runLater(() -> 
            canhbao.thongbao("Thành công", "Cập nhật thông tin nhân sự thành công!")
        );
    }   
    @FXML
    private void suanhansu_trolaiAction() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận quay lại");
        confirm.setHeaderText("Bạn có muốn quay lại danh sách nhân sự?");
        confirm.setContentText("Mọi thay đổi chưa lưu sẽ bị mất.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("nhansu.fxml"));
                    Parent root = loader.load();

                    // Lấy Stage hiện tại
                    Stage stage = (Stage) suanhansu_bttrolai.getScene().getWindow();
                    stage.getScene().setRoot(root);
              

                } catch (IOException e) {
                    e.printStackTrace();
                    canhbao.canhbao("Lỗi điều hướng", "Không thể quay lại màn hình nhân sự.\nChi tiết: " + e.getMessage());
                }
            }
        });
    }
}
