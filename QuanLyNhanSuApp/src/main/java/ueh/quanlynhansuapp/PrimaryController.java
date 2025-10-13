package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.control.Alert;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController {

// KHAI BÁO BIẾN CHO BẢNG PHÒNG BAN  

// Khai báo cho các ô nhập liệu (TextField)
    @FXML private TextField phongban_txma;
    @FXML private TextField phongban_txten;
    @FXML private TextField phongban_txmaTP;
    @FXML private TextField phongban_txsdt;
    @FXML private TextField phongban_txemail;
    @FXML private TextField phongban_txtong;

// Khai báo cho các nút bấm (Button)
    @FXML private Button phongban_btthem;
    @FXML private Button phongban_btxoa;
    @FXML private Button phongban_btsua;
    @FXML private Button phongban_bttimkiem;

// Khai báo cho Bảng (TableView) và các Cột (TableColumn)
    @FXML private TableView<PhongBan> phongban_tbphongban;
    @FXML private TableColumn<PhongBan, String> phongban_colma;
    @FXML private TableColumn<PhongBan, String> phongban_colten;
    @FXML private TableColumn<PhongBan, String> phongban_colmaTP;
    @FXML private TableColumn<PhongBan, String> phongban_colsdt;
    @FXML private TableColumn<PhongBan, String> phongban_colemail;
    @FXML private TableColumn<PhongBan, Integer> phongban_coltong;

    // ================== NHÂN SỰ – FXML CONTROLS ==================
    @FXML private TextField nhansu_txMaNV;
    @FXML private TextField nhansu_txHoTen;
    @FXML private ComboBox<String> nhansu_cbGioiTinh;   // "Nam", "Nữ", "Khác"
    @FXML private DatePicker nhansu_dpNgaySinh;
    @FXML private TextField nhansu_txCCCD;
    @FXML private TextField nhansu_txEmail;
    @FXML private TextField nhansu_txSdt;
    @FXML private ComboBox<String> nhansu_cbPhongBan;   // chứa mã phòng (P00, P01, ... nếu nhóm quy ước)
    @FXML private TextField nhansu_txChucVu;            // nếu FXML có

    @FXML private TableView<NhanSu> nhansu_tbnhansu;
    @FXML private TableColumn<NhanSu, String> nhansu_tbmaNV;
    @FXML private TableColumn<NhanSu, String> nhansu_tbhoTen;
    @FXML private TableColumn<NhanSu, String> nhansu_tbgioiTinh;
    @FXML private TableColumn<NhanSu, LocalDate> nhansu_tbngaySinh;
    @FXML private TableColumn<NhanSu, String> nhansu_tbcccd;
    @FXML private TableColumn<NhanSu, String> nhansu_tbemail;
    @FXML private TableColumn<NhanSu, String> nhansu_tbsdt;
    @FXML private TableColumn<NhanSu, String> nhansu_tbmaPhongBan;
    @FXML private TableColumn<NhanSu, String> nhansu_tbchucVu;

    // ================== DATA MODELS ==================
    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<String> dsMaPhongForCombo = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList(); // field để các hàm xoá dùng

    @FXML
    public void initialize() {  // phần initialize() này tự động chạy khi giao diện được tải lên
                                // và nhiệm vụ của nó là chuẩn bị và cài đặt mọi thứ sẵn sàng

        // BƯỚC 1: KẾT NỐI CÁC CỘT VỚI THUỘC TÍNH DỮ LIỆU (PHÒNG BAN)
        phongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        phongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
        phongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
        // Giữ nguyên theo model hiện nhóm đang dùng:
        phongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        phongban_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        phongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));

        // BƯỚC 2: TẠO DỮ LIỆU MẪU PHÒNG BAN & GÁN VÀO BẢNG
        dsPhongBan.setAll(
            new PhongBan("P00", "Chờ phân công", null, "", "", 0),
            new PhongBan("P01", "Phòng Kỹ thuật", "NV01", "0901234567", "kythuat@cty.com", 15),
            new PhongBan("P02", "Phòng Kinh doanh", "NV03", "0907654321", "kinhdoanh@cty.com", 25),
            new PhongBan("P03", "Phòng Nhân sự", "NV05", "0908888999", "nhansu@cty.com", 5)
        );
        phongban_tbphongban.setItems(dsPhongBan);

        // BƯỚC 3: NẠP COMBOBOX MÃ PHÒNG SAU KHI setItems + LẮNG NGHE THAY ĐỔI
        reloadPhongBanToCombo(); // nạp lần đầu dựa trên dsPhongBan
        dsPhongBan.addListener((ListChangeListener<PhongBan>) c -> reloadPhongBanToCombo());

        // BƯỚC 4: MAP CỘT CHO NHÂN SỰ + GÁN NGUỒN DỮ LIỆU
        nhansu_tbmaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        nhansu_tbhoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        nhansu_tbgioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        nhansu_tbngaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        nhansu_tbcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        nhansu_tbemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        nhansu_tbsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        nhansu_tbmaPhongBan.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
        nhansu_tbchucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        nhansu_tbnhansu.setItems(dsNhanSu);

        // BƯỚC 5: GIÁ TRỊ CỐ ĐỊNH CHO COMBOBOX GIỚI TÍNH
        nhansu_cbGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));

        // BƯỚC 6: NHÂN SỰ – chọn dòng 
        nhansu_tbnhansu.getSelectionModel().selectedItemProperty().addListener((obs, oldV, ns) -> {
            if (ns == null) return;
            nhansu_txMaNV.setText(ns.getMaNV());
            nhansu_txHoTen.setText(ns.getHoTen());
            if (ns.getGioiTinh() != null) nhansu_cbGioiTinh.setValue(ns.getGioiTinh());
            else nhansu_cbGioiTinh.getSelectionModel().clearSelection();
            nhansu_dpNgaySinh.setValue(ns.getNgaySinh());
            nhansu_txCCCD.setText(ns.getCccd());
            nhansu_txEmail.setText(ns.getEmail());
            nhansu_txSdt.setText(ns.getSdt());
            nhansu_cbPhongBan.setValue(ns.getMaPhongBan());
            if (nhansu_txChucVu != null) nhansu_txChucVu.setText(ns.getChucVu());
        });

        // BƯỚC 7: PHÒNG BAN – chọn dòng 
        phongban_tbphongban.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    phongban_txma.setText(newValue.getMaPhong());
                    phongban_txten.setText(newValue.getTenPhong());
                    phongban_txmaTP.setText(newValue.getMaTruongPhong());
                    phongban_txsdt.setText(newValue.getSdt());
                    phongban_txemail.setText(newValue.getEmail());
                    phongban_txtong.setText("" + newValue.getTongSoNhanVien());
                }
            }
        );
    } // kết thúc initialize()

    // ================== XÓA NHÂN SỰ ==================
    @FXML
    private void nhansu_xoaAction() {
        // Bước 1: Lấy nhân viên đang được chọn từ bảng 'nhansu_tbnhansu'
        NhanSu selectedNhanSu = nhansu_tbnhansu.getSelectionModel().getSelectedItem();

        // Kiểm tra xem đã chọn ai chưa
        if (selectedNhanSu == null) {
            hienThongBaoLoi("Chua chon nhan vien", "Vui long chon mot nhan vien đe xoa.");
            return;
        }

        // Bước 2: Kiểm tra xem nhân viên này có phải là trưởng phòng không
        PhongBan phongBanCuaNhanVien = null; // Biến để lưu phòng ban mà nhân viên này làm trưởng phòng
        for (PhongBan pb : dsPhongBan) {
            if (pb.getTruongPhong() != null && pb.getTruongPhong().equals(selectedNhanSu.getMaNV())) {
                phongBanCuaNhanVien = pb; // Tìm thấy rồi, lưu lại và thoát khỏi vòng lặp
                break;
            }
        }

        // Bước 3: Dựa vào kết quả kiểm tra để xử lý theo 2 trường hợp khác nhau
        if (phongBanCuaNhanVien != null) {
            // TRƯỜNG HỢP 1: XÓA TRƯỞNG PHÒNG (Yêu cầu xác nhận nhiều bước)

            // a. Hiển thị CẢNH BÁO ĐẦU TIÊN
            Alert canhBaoDauTien = new Alert(Alert.AlertType.WARNING);
            canhBaoDauTien.setTitle("Canh bao quan trong");
            canhBaoDauTien.setHeaderText("Nhan vien '" + selectedNhanSu.getHoTen() + "' đang là truong phong cua '" 
                                        + phongBanCuaNhanVien.getTenPhong() + "'.");
            canhBaoDauTien.setContentText("Hanh đong nay se khien phong ban khong co nguoi quan ly. Ban co muon tiep tuc?");

            // Chờ người dùng đồng ý cảnh báo đầu tiên
            if (canhBaoDauTien.showAndWait().get() == ButtonType.OK) {

                // b. Hiển thị XÁC NHẬN CUỐI CÙNG
                Alert xacNhanCuoiCung = new Alert(Alert.AlertType.CONFIRMATION);
                xacNhanCuoiCung.setTitle("Xac nhan lan cuoi");
                xacNhanCuoiCung.setHeaderText("Ban co chac chan muon xoa vinh vien nhan vien nay?");

                // Chờ người dùng đồng ý xác nhận cuối cùng
                if (xacNhanCuoiCung.showAndWait().get() == ButtonType.OK) {
                    // c. Thực hiện hành động
                    String tenPhongBan = phongBanCuaNhanVien.getTenPhong();

                    phongBanCuaNhanVien.setTruongPhong(null); // Cập nhật phòng ban
                    dsNhanSu.remove(selectedNhanSu); // Xóa nhân viên

                    // Hiển thị thông báo KẾT QUẢ
                    hienThongBaoThanhCong("Hoan tat", "Da xoa truong phong. Phong ban '" 
                                         + tenPhongBan + "' hien khong co nguoi quan ly.");

                    phongban_tbphongban.refresh();
                }
            }

        } else {
            // TRƯỜNG HỢP 2: XÓA NHÂN VIÊN THƯỜNG (Chỉ cần 1 lần xác nhận)
            Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
            xacNhan.setTitle("Xac nhan xoa");
            xacNhan.setHeaderText("Ban co chac chan muon xoa nhan vien: " + selectedNhanSu.getHoTen() + "?");

            if (xacNhan.showAndWait().get() == ButtonType.OK) {
                dsNhanSu.remove(selectedNhanSu);
                hienThongBaoThanhCong("Thanh cong", "Da xoa nhan vien.");
            }
        }
    }

    // ================== XÓA PHÒNG BAN ==================
    @FXML
    private void phongban_xoaAction() {
        // 1. Lấy phòng ban mà người dùng đang chọn trong bảng
        PhongBan selectedPhongBan = phongban_tbphongban.getSelectionModel().getSelectedItem();

        // 2. Kiểm tra xem người dùng đã chọn phòng ban nào chưa
        if (selectedPhongBan == null) {
            hienThongBaoLoi("Chua chon phong ban", "Vui long chon mot phong ban de xaa.");
            return; // Dừng hàm nếu chưa chọn
        }

        // 3. Hiển thị hộp thoại xác nhận, báo trước về việc tự động chuyển nhân viên
        Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
        xacNhan.setTitle("Xac nhan xoa và di chuyen");
        xacNhan.setHeaderText("Ban co chac chan muon xoa phong ban: " + selectedPhongBan.getTenPhong() + "?");
        xacNhan.setContentText("Luu y: Tat ca nhan vien trong phong nay se duoc tu dong chuyen sang phong 'Cho phan cong'.");

        // 4. Chờ người dùng nhấn OK, nếu đồng ý mới thực hiện
        if (xacNhan.showAndWait().get() == ButtonType.OK) {

            // a. Lưu lại mã của phòng ban sắp bị xóa để dùng cho việc tìm kiếm
            String maPhongCanXoa = selectedPhongBan.getMaPhong();

            // b. Quét qua danh sách nhân viên để tìm và cập nhật
            for (NhanSu ns : dsNhanSu) {
                // Nếu tìm thấy nhân viên thuộc phòng ban sắp bị xóa...
                if (ns.getMaPhong() != null && ns.getMaPhong().equals(maPhongCanXoa)) {
                    // ...thì chuyển họ sang phòng ban mặc định "P00"
                    ns.setMaPhong("P00");
                }
            }

            // c. Sau khi đã chuyển hết nhân viên, tiến hành xóa phòng ban
            dsPhongBan.remove(selectedPhongBan);

            // d. Yêu cầu bảng nhân sự tự làm mới để hiển thị thông tin phòng ban mới
            nhansu_tbnhansu.refresh();

            // e. Hiển thị thông báo hoàn tất
            hienThongBaoThanhCong("Thanh cong", "Da xoa phong ban và di chuyen nhan vien.");
        }
    }

    @FXML
    private void phongban_suaAction() throws IOException {
        phongBan selected = phongban_tbphongban.getSelectionModel().getSelectedItem();
        index = phongban_tbphongban.getItems().indexOf(selected);
        if(selected == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"Chon 1 hang de sua", ButtonType.YES);
            a.setTitle("Thong Tin");
            a.showAndWait();
            return;
        }
         // Tạo FXMLLoader để tải file suaphongban.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("suaphongban.fxml"));
        Parent root = fxmlLoader.load();

        // Lấy controller của giao diện sửa phòng ban
        SuaPhongBan controller = fxmlLoader.getController();
        controller.setData(selected); // Truyền dữ liệu phòng ban được chọn qua màn hình sửa

        // Lấy Stage hiện tại và chuyển scene
        Stage stage = (Stage) phongban_tbphongban.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sửa Phòng Ban");
        stage.show();
    }

    // ================== TIỆN ÍCH DÙNG CHUNG ==================
    private PhongBan timPhongBanTheoMa(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return null;
        ObservableList<PhongBan> items = phongban_tbphongban.getItems();
        if (items == null) return null;
        for (PhongBan pb : items) {
            if (pb != null && maPhong.equalsIgnoreCase(pb.getMaPhong())) return pb;
        }
        return null;
    }

    private NhanSu timNhanSuTheoMa(String maNV) {
        if (maNV == null || maNV.isBlank()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns != null && maNV.equalsIgnoreCase(ns.getMaNV())) return ns;
        }
        return null;
    }

    private boolean isEmailHopLe(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isSdtHopLe(String sdt) {
        if (sdt == null || sdt.isBlank()) return false;
        return sdt.matches("^\\+?\\d{8,15}$");
    }

    private void thongBao(Alert.AlertType type, String title, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }

    private void hienThongBaoLoi(String title, String content) {
        thongBao(Alert.AlertType.ERROR, title, content);
    }

    private void hienThongBaoThanhCong(String title, String content) {
        thongBao(Alert.AlertType.INFORMATION, title, content);
    }

    private void reloadPhongBanToCombo() {
        dsMaPhongForCombo.clear();
        ObservableList<PhongBan> items = phongban_tbphongban.getItems();
        if (items != null) {
            for (PhongBan pb : items) {
                if (pb != null && pb.getMaPhong() != null) {
                    dsMaPhongForCombo.add(pb.getMaPhong());
                }
            }
        }
        nhansu_cbPhongBan.setItems(dsMaPhongForCombo);
    }

    // ================== NHÂN SỰ – THÊM ==================
    @FXML
    private void nhansu_themAction() {
        String maNV     = (nhansu_txMaNV.getText() == null) ? "" : nhansu_txMaNV.getText().trim();
        String hoTen    = (nhansu_txHoTen.getText() == null) ? "" : nhansu_txHoTen.getText().trim();
        String gioiTinh = nhansu_cbGioiTinh.getValue();
        LocalDate ngaySinh = nhansu_dpNgaySinh.getValue();
        String cccd    = (nhansu_txCCCD.getText() == null) ? "" : nhansu_txCCCD.getText().trim();
        String email   = (nhansu_txEmail.getText() == null) ? "" : nhansu_txEmail.getText().trim();
        String sdt     = (nhansu_txSdt.getText() == null) ? "" : nhansu_txSdt.getText().trim();
        String maPhongBan = nhansu_cbPhongBan.getValue();
        String chucVu  = (nhansu_txChucVu != null && nhansu_txChucVu.getText() != null)
                ? nhansu_txChucVu.getText().trim() : null;

        if (maNV.isBlank() || hoTen.isBlank() || ngaySinh == null) {
            thongBao(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Nhập Mã NV, Họ tên và Ngày sinh.");
            return;
        }
        if (timNhanSuTheoMa(maNV) != null) {
            thongBao(Alert.AlertType.WARNING, "Trùng mã", "Mã nhân sự \"" + maNV + "\" đã tồn tại.");
            return;
        }
        if (!email.isBlank() && !isEmailHopLe(email)) {
            thongBao(Alert.AlertType.WARNING, "Email không hợp lệ", "Kiểm tra lại email nhân sự.");
            return;
        }
        if (!sdt.isBlank() && !isSdtHopLe(sdt)) {
            thongBao(Alert.AlertType.WARNING, "SĐT không hợp lệ", "Kiểm tra lại số điện thoại nhân sự.");
            return;
        }
        if (maPhongBan == null || maPhongBan.isBlank()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu phòng ban", "Vui lòng chọn Mã phòng ban.");
            return;
        }
        PhongBan pb = timPhongBanTheoMa(maPhongBan);
        if (pb == null) {
            thongBao(Alert.AlertType.WARNING, "Phòng ban không tồn tại",
                    "Mã phòng \"" + maPhongBan + "\" không có trong danh sách.");
            return;
        }
        if (ngaySinh.isAfter(LocalDate.now())) {
            thongBao(Alert.AlertType.WARNING, "Ngày sinh không hợp lệ", "Ngày sinh không thể ở tương lai.");
            return;
        }

        NhanSu ns = new NhanSu();
        ns.setMaNV(maNV);
        ns.setHoTen(hoTen);
        ns.setGioiTinh(gioiTinh);
        ns.setNgaySinh(ngaySinh);
        ns.setCccd(cccd.isBlank() ? null : cccd);
        ns.setEmail(email.isBlank() ? null : email);
        ns.setSdt(sdt.isBlank() ? null : sdt);
        ns.setMaPhongBan(maPhongBan);
        if (chucVu != null && !chucVu.isBlank()) ns.setChucVu(chucVu);

        dsNhanSu.add(ns);
        nhansu_tbnhansu.refresh();

        // tăng tổng số nhân sự của phòng (không cộng cho P00)
        if (!"P00".equalsIgnoreCase(maPhongBan) && pb.getTongSoNhanVien() < Integer.MAX_VALUE) {
            pb.setTongSoNhanVien(pb.getTongSoNhanVien() + 1);
            phongban_tbphongban.refresh();
        }

        // Xoá trắng form
        nhansu_txMaNV.clear();
        nhansu_txHoTen.clear();
        nhansu_cbGioiTinh.getSelectionModel().clearSelection();
        nhansu_dpNgaySinh.setValue(null);
        nhansu_txCCCD.clear();
        nhansu_txEmail.clear();
        nhansu_txSdt.clear();
        nhansu_cbPhongBan.getSelectionModel().clearSelection();
        if (nhansu_txChucVu != null) nhansu_txChucVu.clear();

        thongBao(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm nhân sự \"" + hoTen + "\".");
    }

    // ================== PHÒNG BAN – THÊM ==================
    @FXML
    private void phongban_themAction() {
        String maPhong     = (phongban_txma.getText() == null) ? "" : phongban_txma.getText().trim();
        String tenPhong    = (phongban_txten.getText() == null) ? "" : phongban_txten.getText().trim();
        String maTP        = (phongban_txmaTP.getText() == null) ? "" : phongban_txmaTP.getText().trim();
        String sdtPhong    = (phongban_txsdt.getText() == null) ? "" : phongban_txsdt.getText().trim();
        String emailPhong  = (phongban_txemail.getText() == null) ? "" : phongban_txemail.getText().trim();
        String tongStr     = (phongban_txtong.getText() == null) ? "" : phongban_txtong.getText().trim();

        if (maPhong.isBlank() || tenPhong.isBlank()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Nhập Mã phòng và Tên phòng.");
            return;
        }
        if (timPhongBanTheoMa(maPhong) != null) {
            thongBao(Alert.AlertType.WARNING, "Trùng mã", "Mã phòng \"" + maPhong + "\" đã tồn tại.");
            return;
        }
        if (!emailPhong.isBlank() && !isEmailHopLe(emailPhong)) {
            thongBao(Alert.AlertType.WARNING, "Email không hợp lệ", "Kiểm tra lại email phòng ban.");
            return;
        }
        if (!sdtPhong.isBlank() && !isSdtHopLe(sdtPhong)) {
            thongBao(Alert.AlertType.WARNING, "SĐT không hợp lệ", "Kiểm tra lại số điện thoại phòng ban.");
            return;
        }
        if (!maTP.isBlank() && timNhanSuTheoMa(maTP) == null) {
            thongBao(Alert.AlertType.WARNING, "Sai trưởng phòng",
                    "Mã nhân sự \"" + maTP + "\" không tồn tại để làm Trưởng phòng.");
            return;
        }
        int tong = 0;
        if (!tongStr.isBlank()) {
            try {
                tong = Integer.parseInt(tongStr);
                if (tong < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                thongBao(Alert.AlertType.WARNING, "Tổng nhân sự không hợp lệ", "Tổng phải là số nguyên không âm.");
                return;
            }
        }

        PhongBan pb = new PhongBan();
        pb.setMaPhong(maPhong);
        pb.setTenPhong(tenPhong);
        pb.setMaTruongPhong(maTP.isBlank() ? null : maTP);
        pb.setSdtPhong(sdtPhong.isBlank() ? null : sdtPhong);
        pb.setEmailPhong(emailPhong.isBlank() ? null : emailPhong);
        pb.setTongSoNhanVien(tong);

        // Thêm vào bảng phòng ban đang hiển thị
        phongban_tbphongban.getItems().add(pb);
        phongban_tbphongban.refresh();

        // cập nhật combobox mã phòng ở tab Nhân sự
        reloadPhongBanToCombo();

        // Xoá trắng form
        phongban_txma.clear();
        phongban_txten.clear();
        phongban_txmaTP.clear();
        phongban_txsdt.clear();
        phongban_txemail.clear();
        phongban_txtong.clear();

        thongBao(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm phòng ban \"" + tenPhong + "\".");
    }
}
