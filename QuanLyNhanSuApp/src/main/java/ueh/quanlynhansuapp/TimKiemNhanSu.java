/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


/*
    Controller cho giao diện "Tìm kiếm nhân sự", phân quyền Admin (timkiemnhansu.fxml)
    Chức năng:
    - Cho phép nhập mã nhân viên để tìm thông tin
    - Hiển thị kết quả trong bảng
    - Chỉ cho phép xem, không chỉnh sửa dữ liệu
*/
public class TimKiemNhanSu {
    @FXML private TextField timkiemnhansu_txma;
    @FXML private TextField timkiemnhansu_txten;
    @FXML private TextField timkiemnhansu_txcccd;
    @FXML private TextField timkiemnhansu_txemail;
    @FXML private TextField timkiemnhansu_txsdt;
    
    // Các combobox và ngày tháng
    @FXML private ComboBox<String> timkiemnhansu_cbogioitinh;
    @FXML private DatePicker timkiemnhansu_datengaysinh;
    @FXML private ComboBox<PhongBan> timkiemnhansu_cbmaPB;
    @FXML private ComboBox<String> timkiemnhansu_cbchucvu;

    // Các nút bấm
    @FXML private Button timkiemnhansu_bttimkiem;
    @FXML private Button timkiemnhansu_bttrolai;

    // Bảng và các cột
    @FXML private TableView<NhanSu> timkiemnhansu_tbnhansu;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colma;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colten;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colgioitinh;
    @FXML private TableColumn<NhanSu, LocalDate> timkiemnhansu_colngaysinh;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colcccd;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colemail;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colsdt;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colmaPB;
    @FXML private TableColumn<NhanSu, String> timkiemnhansu_colchucvu;

    // danh sách dữ liệu gốc và kết quả tìm kiếm
    private ObservableList<NhanSu> masterData = FXCollections.observableArrayList();
    private ObservableList<NhanSu> ketQuaTimKiem = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Kết nối các cột trong bảng với các thuộc tính của lớp NhanSu
        timkiemnhansu_colma.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        timkiemnhansu_colten.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        timkiemnhansu_colgioitinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        timkiemnhansu_colngaysinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        timkiemnhansu_colcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        timkiemnhansu_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        timkiemnhansu_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        timkiemnhansu_colmaPB.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
        timkiemnhansu_colchucvu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // Cài đặt các giá trị cố định cho các ComboBox
        timkiemnhansu_cbogioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        
        // Khóa các ô thông tin, chỉ hiển thị
        setInfoFieldsEditable(false);
        
        // Liên kết bảng với danh sách kết quả
        timkiemnhansu_tbnhansu.setItems(ketQuaTimKiem);
        
        // hiển thị mã và tên phòng ban trong comboBox
        timkiemnhansu_cbmaPB.setCellFactory(param -> new ListCell<PhongBan>() {
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

    timkiemnhansu_cbmaPB.setButtonCell(new ListCell<PhongBan>() {
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
        
    }

    // hàm khóa hoặc mở khóa các ô nhập
    private void setInfoFieldsEditable(boolean editable) {
        timkiemnhansu_txten.setEditable(editable);
        timkiemnhansu_txcccd.setEditable(editable);
        timkiemnhansu_txemail.setEditable(editable);
        timkiemnhansu_txsdt.setEditable(editable);
        // Không dùng setDisable để tránh bị mờ
        timkiemnhansu_cbogioitinh.setMouseTransparent(!editable);
        timkiemnhansu_cbogioitinh.setFocusTraversable(editable);

        timkiemnhansu_datengaysinh.setMouseTransparent(!editable);
        timkiemnhansu_datengaysinh.setFocusTraversable(editable);

        timkiemnhansu_cbmaPB.setMouseTransparent(!editable);
        timkiemnhansu_cbmaPB.setFocusTraversable(editable);

        timkiemnhansu_cbchucvu.setMouseTransparent(!editable);
        timkiemnhansu_cbchucvu.setFocusTraversable(editable);
    }

    // nhận dữ liệu từ màn hình chính
    public void setData(ObservableList<NhanSu> allNhanSu, ObservableList<PhongBan> dsPhongBan, ObservableList<String> allChucVu) {
        this.masterData.setAll(allNhanSu);
        timkiemnhansu_cbmaPB.setItems(dsPhongBan);
        timkiemnhansu_cbchucvu.setItems(allChucVu);
    }

    // Nút "Tìm kiếm"
    @FXML
    private void timkiemnhansu_timkiemAction() {
        String maNV_input = timkiemnhansu_txma.getText().trim();
        // Kiểm tra mã nhân viên có được nhập hay không
        if (maNV_input.isEmpty()) {
            canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập Mã nhân viên để tìm kiếm!");
            return;
        }

        // Tìm nhân viên theo mã
        NhanSu ketQua = null;
        for (NhanSu ns : masterData) {
            if (ns.getMaNV() != null && ns.getMaNV().equalsIgnoreCase(maNV_input)) {
                ketQua = ns;
                break;
            }
        }

        if (ketQua == null) {
            canhbao.thongbao("Không tìm thấy", "Không tồn tại nhân viên có mã \"" + maNV_input + "\"");
            return;
        }

        // Kiểm tra nếu nhân viên này đã nằm trong danh sách tìm rồi thì không thêm nữa
        boolean tonTaiTrongBang = ketQuaTimKiem.stream()
            .anyMatch(ns -> ns.getMaNV() != null && ns.getMaNV().equalsIgnoreCase(maNV_input));

        if (!tonTaiTrongBang) {
            ketQuaTimKiem.add(ketQua);
        } else {
            canhbao.thongbao("Thông báo", "Nhân viên này đã có trong bảng kết quả.");
        }

        // Hiển thị thông tin nhân viên lên form
        showNhanSuInfo(ketQua);
        timkiemnhansu_tbnhansu.getSelectionModel().select(ketQua);
    }

    // Hàm hiển thị thông tin nhân viên tìm được lên form
    private void showNhanSuInfo(NhanSu ns) {
        timkiemnhansu_txten.setText(ns.getHoTen());
        timkiemnhansu_txcccd.setText(ns.getCccd());
        timkiemnhansu_txemail.setText(ns.getEmail());
        timkiemnhansu_txsdt.setText(ns.getSdt());
        timkiemnhansu_cbogioitinh.setValue(ns.getGioiTinh());
        timkiemnhansu_datengaysinh.setValue(ns.getNgaySinh());
        timkiemnhansu_cbmaPB.setValue(DataService.getInstance().timPhongBanTheoMa(ns.getMaPhongBan()));
        timkiemnhansu_cbchucvu.setValue(ns.getChucVu());
    }

     // Hàm xóa thông tin trong form
    private void clearInfoFields() {
        timkiemnhansu_txten.clear();
        timkiemnhansu_txcccd.clear();
        timkiemnhansu_txemail.clear();
        timkiemnhansu_txsdt.clear();
        timkiemnhansu_cbogioitinh.setValue(null);
        timkiemnhansu_datengaysinh.setValue(null);
        timkiemnhansu_cbmaPB.setValue(null);
        timkiemnhansu_cbchucvu.setValue(null);
    }
 
    // Nút "Trở lại"
    @FXML
    private void timkiemnhansu_trolaiAction() throws IOException {
        // Hỏi xác nhận
        boolean dongY = canhbao.xacNhan(
            "Trở lại",
            "Bạn có chắc chắn muốn quay về màn hình Nhân sự?",
            "Mọi kết quả tìm kiếm hiện tại sẽ bị mất."
        );
        if (!dongY) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("nhansu.fxml"));
        Parent root = loader.load();
        
        NhanSuController controller = loader.getController();
        controller.initialize();

        // Chuyển scene
        timkiemnhansu_bttrolai.getScene().setRoot(root);
    }
}
