package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

// Controller của giao diện sửa nhân sự, phân quyển Admin (suanhansu.fxml)
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
    
    // Map chứa danh sách chức vụ cho từng phòng ban
    private final Map<String, List<String>> phongBanToChucVuMap = new HashMap<>();

    // Hàm khởi tạo dữ liệu chức vụ cho từng phòng ban
    private void initializeChucVuData() {
        phongBanToChucVuMap.put("Phòng Kế toán", Arrays.asList("Trưởng phòng Kế toán", "Phó phòng Kế toán", "Kế toán trưởng", "Kế toán tổng hợp", "Kế toán viên", "Kế toán kho", "Kế toán thuế", "Thực tập sinh Kế toán"));
        phongBanToChucVuMap.put("Phòng Nhân sự", Arrays.asList("Trưởng phòng Nhân sự", "Phó phòng Nhân sự", "Chuyên viên Tuyển dụng", "Chuyên viên C&B", "Chuyên viên Đào tạo", "Thực tập sinh Nhân sự"));
        phongBanToChucVuMap.put("Phòng Kỹ thuật", Arrays.asList("Trưởng phòng IT", "Lập trình viên Backend", "Lập trình viên Frontend", "UI/UX Designer", "Quản trị hệ thống", "Nhân viên Hỗ trợ IT", "Thực tập sinh IT"));
        phongBanToChucVuMap.put("Phòng Kinh doanh", Arrays.asList("Trưởng phòng Kinh doanh", "Phó phòng Kinh doanh", "Nhân viên kinh doanh", "Trợ lý kinh doanh", "Nhân viên Tele-sales", "Thực tập sinh Kinh doanh"));
        phongBanToChucVuMap.put("Phòng Marketing", Arrays.asList("Trưởng phòng Marketing", "Chuyên viên Digital Marketing", "Chuyên viên Content Marketing", "Nhân viên thiết kế", "Chuyên viên SEO/SEM", "Thực tập sinh Marketing"));
        phongBanToChucVuMap.put("Chờ phân công", Arrays.asList("Chưa có chức vụ")); // Dành cho P00
    }
    
    // nhận dữ liệu nhân sự cần sửa từ giao diện chính
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
        // Gọi hàm nạp chức vụ theo phòng ban
        initializeChucVuData();

        // Khi chọn phòng ban thì load danh sách chức vụ tương ứng
        suanhansu_cbmaPB.valueProperty().addListener((obs, oldPB, newPB) -> {
            if (newPB != null) {
                String tenPhong = newPB.getTenPhong();
                List<String> chucVuList = phongBanToChucVuMap.getOrDefault(
                    tenPhong, Collections.singletonList("Chưa có chức vụ"));
                suanhansu_cbchucvu.setItems(FXCollections.observableArrayList(chucVuList));
                suanhansu_cbchucvu.getSelectionModel().clearSelection();
            }
        });

        // Set danh sách chức vụ tương ứng với phòng ban hiện tại
        PhongBan phongBanHienTai = DataService.getInstance().timPhongBanTheoMa(ns.getMaPhongBan());
        if (phongBanHienTai != null) {
            String tenPhong = phongBanHienTai.getTenPhong();
            List<String> chucVuList = phongBanToChucVuMap.getOrDefault(
                tenPhong, Collections.singletonList("Chưa có chức vụ"));
            suanhansu_cbchucvu.setItems(FXCollections.observableArrayList(chucVuList));
        }

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
    
    // Nút "Sửa"
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
    
        // Kiểm tra trường bắt buộc
        if (hoTen.isEmpty() || gioiTinh == null || gioiTinh.isEmpty() || ngaySinh == null || cccd.isEmpty() || email.isEmpty() || sdt.isEmpty()|| selectedPB == null || chucVu == null || chucVu.isEmpty()) {
            canhbao.canhbao("Thiếu thông tin",
                "Vui lòng điền đầy đủ tất cả thông tin!");
            return;
        }

        // Kiểm tra cccd
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

        // Kiểm tra email
        if (!email.isEmpty()) {
            boolean emailTrung = dataService.getDsNhanSu().stream()
                .anyMatch(p -> !p.getMaNV().equals(ns.getMaNV()) && email.equalsIgnoreCase(p.getEmail()));
            if (emailTrung) {
                canhbao.canhbao("Trùng Email", "Email \"" + email + "\" đã được sử dụng bởi nhân viên khác.");
                return;
            }
        }

        // Kiểm tra SĐT 
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


        // Kiểm tra trùng chức vụ đặc biệt trong cùng phòng ban
        if (chucVu.toLowerCase().contains("trưởng phòng") || chucVu.toLowerCase().contains("phó phòng")) {
            String maPhongBanMoi = selectedPB.getMaPhong();

            boolean daCoNguoiGiuChucVu = dataService.getDsNhanSu().stream()
                .anyMatch(p ->
                    !p.getMaNV().equals(ns.getMaNV()) && // loại chính nhân viên đang sửa
                    p.getMaPhongBan().equalsIgnoreCase(maPhongBanMoi) &&
                    p.getChucVu() != null &&
                    p.getChucVu().equalsIgnoreCase(chucVu)
                );

            if (daCoNguoiGiuChucVu) {
                canhbao.canhbao(
                    "Trùng chức vụ",
                    "Phòng \"" + selectedPB.getTenPhong() + "\" đã có " + chucVu.toLowerCase() + ".\n" +
                    "Không thể gán thêm người với chức vụ này."
                );
                return;
            }
        }
        // Kiểm tra xem chức vụ có thuộc phòng ban đã chọn không
        if (chucVu == null || chucVu.isEmpty() || chucVu.contains("Chưa có chức vụ")) {
             canhbao.canhbao("Thiếu chức vụ", "Vui lòng chọn chức vụ cho nhân sự.");
            return;
        }

         // Popup xác nhận trước khi lưu thay đổi
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận sửa thông tin");
        confirm.setHeaderText("Bạn có chắc chắn muốn lưu thay đổi?");
        confirm.setContentText("Nhân sự: " + ns.getMaNV() + " - " + ns.getHoTen());
        ButtonType yesBtn = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yesBtn, cancelBtn);

        confirm.showAndWait().ifPresent(response -> {
            if (response == yesBtn) {
                // --- Nếu người dùng xác nhận ---
                ns.setHoTen(hoTen);
                ns.setGioiTinh(gioiTinh);
                ns.setNgaySinh(ngaySinh);
                ns.setCccd(cccd);
                ns.setEmail(email);
                ns.setSdt(sdt);
                ns.setMaPhongBan(selectedPB.getMaPhong());
                ns.setChucVu(chucVu);

                dataService.updateNhanSu(ns);
                canhbao.thongbao("Thành công", "Cập nhật thông tin nhân sự thành công!");

                // Đóng cửa sổ sửa (Stage hiện tại)
                Stage currentStage = (Stage) suanhansu_btsua.getScene().getWindow();
                currentStage.close();
            } else {
                canhbao.thongbao("Đã hủy", "Không có thay đổi nào được lưu.");
            }
        });
    }
    
    // Nút "Trở lại"
    @FXML
    private void suanhansu_trolaiAction() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận quay lại");
        confirm.setHeaderText("Bạn có muốn quay lại danh sách nhân sự?");
        confirm.setContentText("Mọi thay đổi chưa lưu sẽ bị mất.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage currentStage = (Stage) suanhansu_bttrolai.getScene().getWindow();
                currentStage.close(); // Đóng form popup
            }
        });
    }
}
