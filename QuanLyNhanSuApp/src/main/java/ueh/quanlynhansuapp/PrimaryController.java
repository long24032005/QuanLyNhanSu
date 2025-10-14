package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
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

    // ================== NHÂN SỰ ==================
    @FXML private TextField nhansu_txma;
    @FXML private TextField nhansu_txten;
    @FXML private ComboBox<String> nhansu_cbogioitinh;    // dùng ComboBox cho thuận tiện
    @FXML private DatePicker nhansu_datengaysinh;
    @FXML private TextField nhansu_txcccd;
    @FXML private TextField nhansu_txemail;
    @FXML private TextField nhansu_txsdt;
    @FXML private ComboBox<String> nhansu_cbmaPB;         // mã phòng ban
    @FXML private ComboBox<String> nhansu_cbchucvu;       // chức vụ

    @FXML private TableView<NhanSu> nhansu_tbnhansu;
    @FXML private TableColumn<NhanSu, String> nhansu_colma;
    @FXML private TableColumn<NhanSu, String> nhansu_colten;
    @FXML private TableColumn<NhanSu, String> nhansu_colgioitinh;
    @FXML private TableColumn<NhanSu, LocalDate> nhansu_colngaysinh;
    @FXML private TableColumn<NhanSu, String> nhansu_colcccd;
    @FXML private TableColumn<NhanSu, String> nhansu_colemail;
    @FXML private TableColumn<NhanSu, String> nhansu_colsdt;
    @FXML private TableColumn<NhanSu, String> nhansu_colmaPB;
    @FXML private TableColumn<NhanSu, String> nhansu_colchucvu;

    // Nút (khai báo theo list object của nhóm)
    @FXML private Button nhansu_btthem;
    @FXML private Button nhansu_btxoa;
    @FXML private Button nhansu_btsua;
    @FXML private Button nhansu_bttimkiem;

    // ================== NHÂN SỰ – DATA MODELS ==================
    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<String> dsMaPhongForCombo = FXCollections.observableArrayList();

    @FXML
    public void initialize() {  // phần initialize() này tự động chạy khi giao diện được tải lên
                                // và nhiệm vụ của nó là chuẩn bị và cài đặt mọi thứ sẵn sàng
        
    // BƯỚC 1: KẾT NỐI CÁC CỘT VỚI THUỘC TÍNH DỮ LIỆU
    // Ra lệnh cho mỗi cột biết nó cần lấy dữ liệu từ thuộc tính nào của lớp PhongBan
    phongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
    phongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
    phongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
    phongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
    phongban_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
    phongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));

    // ---------- NHÂN SỰ: map cột (đúng tên cột của nhóm) ----------
    nhansu_colma.setCellValueFactory(new PropertyValueFactory<>("maNV"));
    nhansu_colten.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
    nhansu_colgioitinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
    nhansu_colngaysinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
    nhansu_colcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
    nhansu_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
    nhansu_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
    nhansu_colmaPB.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
    nhansu_colchucvu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

    // ---------- NHÂN SỰ: gán nguồn dữ liệu cho bảng ----------
    nhansu_tbnhansu.setItems(dsNhanSu);

    // ---------- NHÂN SỰ: combobox giá trị cố định ----------
    nhansu_cbogioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
    // Gợi ý danh sách chức vụ phổ biến (tuỳ nhóm có thể chỉnh trong FXML)
    nhansu_cbchucvu.setItems(FXCollections.observableArrayList("Nhân viên", "Trưởng phòng", "Phó phòng", "Thực tập"));

    // ---------- NHÂN SỰ: nạp mã phòng vào combobox từ bảng Phòng ban ----------
    reloadPhongBanToCombo(); // lần đầu
    // Tự động cập nhật combobox khi danh sách phòng ban thay đổi
    if (phongban_tbphongban.getItems() != null) {
        phongban_tbphongban.getItems().addListener((ListChangeListener<PhongBan>) c -> reloadPhongBanToCombo());
    }

    // ---------- NHÂN SỰ: chọn dòng -> đổ về form ----------
    nhansu_tbnhansu.getSelectionModel().selectedItemProperty().addListener((obs, oldV, ns) -> {
        if (ns == null) return;
        nhansu_txma.setText(ns.getMaNV());
        nhansu_txten.setText(ns.getHoTen());
        if (ns.getGioiTinh() != null) nhansu_cbogioitinh.setValue(ns.getGioiTinh());
        else nhansu_cbogioitinh.getSelectionModel().clearSelection();
        nhansu_datengaysinh.setValue(ns.getNgaySinh());
        nhansu_txcccd.setText(ns.getCccd());
        nhansu_txemail.setText(ns.getEmail());
        nhansu_txsdt.setText(ns.getSdt());
        nhansu_cbmaPB.setValue(ns.getMaPhongBan());
        if (ns.getChucVu() != null) nhansu_cbchucvu.setValue(ns.getChucVu());
        else nhansu_cbchucvu.getSelectionModel().clearSelection();
    });

    // BƯỚC 2: TẠO DỮ LIỆU MẪU VÀ HIỂN THỊ LÊN BẢNG 
    // (Sau này bạn sẽ thay phần này bằng code lấy dữ liệu từ database)
    ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList(
        new PhongBan("P00", "Chờ phân công", null, "", "", 0),
        new PhongBan("P01", "Phòng Kỹ thuật", "NV01", "0901234567", "kythuat@cty.com", 15),
        new PhongBan("P02", "Phòng Kinh doanh", "NV03", "0907654321", "kinhdoanh@cty.com", 25),
        new PhongBan("P03", "Phòng Nhân sự", "NV05", "0908888999", "nhansu@cty.com", 5)
    );
    
    // Ra lệnh cho cái bảng hiển thị danh sách vừa tạo
    phongban_tbphongban.setItems(dsPhongBan);
    
    // BƯỚC 3: THEO DÕI KHI NGƯỜI DÙNG BẤM VÀO BẢNG 
    // Thêm một "công cụ" vào bảng để theo dõi xem người dùng có chọn dòng nào không
    phongban_tbphongban.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
            // Khi người dùng bấm vào một dòng, `newValue` chính là phòng ban được chọn
            if (newValue != null) {
                // Lấy thông tin từ phòng ban được chọn và điền vào các ô nhập liệu
                phongban_txma.setText(newValue.getMaPhong());
                phongban_txten.setText(newValue.getTenPhong());
                phongban_txmaTP.setText(newValue.getMaTruongPhong());
                phongban_txsdt.setText(newValue.getSdt());
                phongban_txemail.setText(newValue.getEmail());
                // Chuyển số thành chữ để hiển thị. Dấu "" + ... là một mẹo đơn giản.
                phongban_txtong.setText("" + newValue.getTongSoNhanVien()); 
            }
        }
    );

} //Dấu ngoặc nhọn này là kết thúc của phần initialize()

    
  

 // Cách hoạt động của  nút Xóa ở bảng Nhân sự
 // 1. Kiểm tra xem nhân viên bạn muốn xóa có phải là trưởng phòng không
 // 2. NẾU NHÂN VIÊN BẠN MUỐN XÓA LÀ TRƯỞNG PHÒNG:
 // a. Hiển thị cảnh báo đầu tiên: "Đây là trưởng phòng... Bạn có muốn tiếp tục?"
 // b. Nếu đồng ý -> Hiển thị xác nhận cuối cùng: "Bạn có chắc chắn muốn xóa?"
 // c. Nếu đồng ý -> Xóa, cập nhật phòng ban, và thông báo phòng ban trống trưởng phòng
 // 3. NẾU NHÂN VIÊN BẠN MUỐN XÓA LÀ NHÂN VIÊN THƯỜNG:
 // a. Hiển thị xác nhận cuối cùng: "Bạn có chắc chắn muốn xóa?"
 // b. Nếu đồng ý -> Xóa và thông báo thành công

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

    


 // Cách hoạt động của  nút Xóa ở bảng Phòng ban
 // Tự động chuyển tất cả nhân viên trong phòng ban bị xóa sang phòng "Chờ phân công" (mã P00) trước khi xóa
 
 
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
        canhbao.thongbao("Thông báo", "bạn đang vào chức năng sửa");
        App.setRoot("suaphongban");
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
        // đổi sang đúng fx:id nhóm
        nhansu_cbmaPB.setItems(dsMaPhongForCombo);
    }

    // ================== NHÂN SỰ – THÊM  ==================
    @FXML
    private void nhansu_themAction() {
        String maNV     = (nhansu_txma.getText() == null) ? "" : nhansu_txma.getText().trim();
        String hoTen    = (nhansu_txten.getText() == null) ? "" : nhansu_txten.getText().trim();
        String gioiTinh = nhansu_cbogioitinh.getValue();
        LocalDate ngaySinh = nhansu_datengaysinh.getValue();
        String cccd    = (nhansu_txcccd.getText() == null) ? "" : nhansu_txcccd.getText().trim();
        String email   = (nhansu_txemail.getText() == null) ? "" : nhansu_txemail.getText().trim();
        String sdt     = (nhansu_txsdt.getText() == null) ? "" : nhansu_txsdt.getText().trim();
        String maPhongBan = nhansu_cbmaPB.getValue();
        String chucVu  = (nhansu_cbchucvu != null) ? nhansu_cbchucvu.getValue() : null;

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

        // tăng tổng số nhân sự của phòng
        if (pb.getTongSoNhanVien() < Integer.MAX_VALUE) {
            pb.setTongSoNhanVien(pb.getTongSoNhanVien() + 1);
            phongban_tbphongban.refresh();
        }

        // Xoá trắng form
        nhansu_txma.clear();
        nhansu_txten.clear();
        nhansu_cbogioitinh.getSelectionModel().clearSelection();
        nhansu_datengaysinh.setValue(null);
        nhansu_txcccd.clear();
        nhansu_txemail.clear();
        nhansu_txsdt.clear();
        nhansu_cbmaPB.getSelectionModel().clearSelection();
        if (nhansu_cbchucvu != null) nhansu_cbchucvu.getSelectionModel().clearSelection();

        thongBao(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm nhân sự \"" + hoTen + "\".");
    }

    // ================== PHÒNG BAN – THÊM  ==================
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
        pb.setSdt(sdtPhong.isBlank() ? null : sdtPhong);       // ← dùng setSdt(...)
        pb.setEmail(emailPhong.isBlank() ? null : emailPhong); // ← dùng setEmail(...)
        pb.setTongSoNhanVien(tong);

        phongban_tbphongban.getItems().add(pb);
        phongban_tbphongban.refresh();
        reloadPhongBanToCombo();

        phongban_txma.clear();
        phongban_txten.clear();
        phongban_txmaTP.clear();
        phongban_txsdt.clear();
        phongban_txemail.clear();
        phongban_txtong.clear();

        thongBao(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm phòng ban \"" + tenPhong + "\".");
    }

}

