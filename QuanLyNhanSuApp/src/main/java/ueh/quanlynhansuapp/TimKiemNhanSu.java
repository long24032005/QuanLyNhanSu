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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 *
 * @author lagia
 */
public class TimKiemNhanSu {
    @FXML private TextField timkiemnhansu_txma;
    @FXML private TextField timkiemnhansu_txten;
    @FXML private TextField timkiemnhansu_txcccd;
    @FXML private TextField timkiemnhansu_txemail;
    @FXML private TextField timkiemnhansu_txsdt;
    
    // Các hộp lựa chọn và ngày tháng
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

    private ObservableList<NhanSu> masterData = FXCollections.observableArrayList();
    private ObservableList<NhanSu> ketQuaTimKiem = FXCollections.observableArrayList();



    @FXML
    public void initialize() {
        // 1. Kết nối các cột trong bảng với các thuộc tính của lớp NhanSu
        timkiemnhansu_colma.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        timkiemnhansu_colten.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        timkiemnhansu_colgioitinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        timkiemnhansu_colngaysinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        timkiemnhansu_colcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        timkiemnhansu_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        timkiemnhansu_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        timkiemnhansu_colmaPB.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
        timkiemnhansu_colchucvu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // 2. Cài đặt các giá trị cố định cho các ComboBox
        timkiemnhansu_cbogioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        
        // Khóa các ô thông tin, chỉ hiển thị
        setInfoFieldsEditable(false);
        
        // Liên kết bảng với danh sách kết quả
        timkiemnhansu_tbnhansu.setItems(ketQuaTimKiem);
        
    }

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

    public void setData(ObservableList<NhanSu> allNhanSu, ObservableList<PhongBan> dsPhongBan, ObservableList<String> allChucVu) {
        this.masterData.setAll(allNhanSu);
        timkiemnhansu_cbmaPB.setItems(dsPhongBan);
        timkiemnhansu_cbchucvu.setItems(allChucVu);
    }

    @FXML
    private void timkiemnhansu_timkiemAction() {
        String maNV_input = timkiemnhansu_txma.getText().trim();
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

        // Hiển thị thông tin lên form
        showNhanSuInfo(ketQua);
        timkiemnhansu_tbnhansu.getSelectionModel().select(ketQua);
    }

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

    @FXML
    private void timkiemnhansu_lammoisAction() {
        timkiemnhansu_txma.clear();
        ketQuaTimKiem.clear();
        clearInfoFields();
        canhbao.thongbao("Đã làm mới", "Bảng kết quả tìm kiếm đã được xóa.");
    }
        /* Code cũ
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
        
        timkiemnhansu_tbnhansu.getSelectionModel().selectedItemProperty().addListener((obs, oldV, ns) -> {
            if (ns != null) {
                // Hiển thị dữ liệu của nhân sự (ns) lên các ô tìm kiếm
                timkiemnhansu_txma.setText(ns.getMaNV());
                timkiemnhansu_txten.setText(ns.getHoTen());
                timkiemnhansu_txcccd.setText(ns.getCccd());
                timkiemnhansu_txemail.setText(ns.getEmail());
                timkiemnhansu_txsdt.setText(ns.getSdt());
                timkiemnhansu_cbogioitinh.setValue(ns.getGioiTinh());
                timkiemnhansu_datengaysinh.setValue(ns.getNgaySinh());
                timkiemnhansu_cbmaPB.setValue(DataService.getInstance().timPhongBanTheoMa(ns.getMaPhongBan()));
                timkiemnhansu_cbchucvu.setValue(ns.getChucVu());
            }
        });



        
        // Khóa các ô lại SAU KHI tìm kiếm
        timkiemnhansu_txten.setEditable(false);
        timkiemnhansu_txcccd.setEditable(false);
        timkiemnhansu_txemail.setEditable(false);
        timkiemnhansu_txsdt.setEditable(false);
        
        // Khóa ComboBox và DatePicker (theo yêu cầu: không mờ)
        timkiemnhansu_cbogioitinh.setMouseTransparent(true);
        timkiemnhansu_cbogioitinh.setFocusTraversable(false);

        timkiemnhansu_datengaysinh.setMouseTransparent(true);
        timkiemnhansu_datengaysinh.setFocusTraversable(false);
        
        timkiemnhansu_cbmaPB.setMouseTransparent(true);
        timkiemnhansu_cbmaPB.setFocusTraversable(false);

        timkiemnhansu_cbchucvu.setMouseTransparent(true);
        timkiemnhansu_cbchucvu.setFocusTraversable(false);


        
        // Khóa các ô lại SAU KHI tìm kiếm
        timkiemnhansu_txten.setEditable(false);
        timkiemnhansu_txcccd.setEditable(false);
        timkiemnhansu_txemail.setEditable(false);
        timkiemnhansu_txsdt.setEditable(false);
        
        // Khóa ComboBox và DatePicker (theo yêu cầu: không mờ)
        timkiemnhansu_cbogioitinh.setMouseTransparent(true);
        timkiemnhansu_cbogioitinh.setFocusTraversable(false);

        timkiemnhansu_datengaysinh.setMouseTransparent(true);
        timkiemnhansu_datengaysinh.setFocusTraversable(false);
        
        timkiemnhansu_cbmaPB.setMouseTransparent(true);
        timkiemnhansu_cbmaPB.setFocusTraversable(false);

        timkiemnhansu_cbchucvu.setMouseTransparent(true);
        timkiemnhansu_cbchucvu.setFocusTraversable(false);


       
    }

    public void setData(ObservableList<NhanSu> allNhanSu, ObservableList<PhongBan> dsPhongBan, ObservableList<String> allChucVu) {
        // Sao chép dữ liệu vào danh sách gốc của màn hình này
        this.masterData.setAll(allNhanSu);
        
        // Hiển thị toàn bộ nhân sự lên bảng lúc ban đầu
        timkiemnhansu_tbnhansu.setItems(masterData);
        
        // Nạp danh sách mã phòng ban vào ComboBox
        timkiemnhansu_cbmaPB.setItems(dsPhongBan); // THAY ĐỔI
        
        timkiemnhansu_cbchucvu.setItems(allChucVu);
    }

    @FXML
    private void timkiemnhansu_timkiemAction() {
        // Lấy tất cả các tiêu chí tìm kiếm từ người dùng
        String maNV_input = timkiemnhansu_txma.getText().trim().toLowerCase();
        String hoTen = timkiemnhansu_txten.getText().trim().toLowerCase();
        String cccd = timkiemnhansu_txcccd.getText().trim().toLowerCase();
        String email = timkiemnhansu_txemail.getText().trim().toLowerCase();
        String sdt = timkiemnhansu_txsdt.getText().trim().toLowerCase();
        String gioiTinh = timkiemnhansu_cbogioitinh.getValue();
        LocalDate ngaySinh = timkiemnhansu_datengaysinh.getValue();
        PhongBan selectedPB = timkiemnhansu_cbmaPB.getValue();
        String maPB = (selectedPB != null) ? selectedPB.getMaPhong() : null; // Lấy mã
        String chucVu = timkiemnhansu_cbchucvu.getValue();
        
        // 1. Kiểm tra Bỏ trống mnv => warning
        if (maNV_input.isEmpty()) {
            // Sửa "mssv" thành "Mã nhân viên" cho chính xác
            canhbao.canhbao("Thiếu thông tin", "Mã nhân viên không được để trống!");
            return; // Dừng hàm, không tìm kiếm nữa
        }
        
        String maNV = maNV_input.toLowerCase();

        // 2. Kiểm tra Nhập mnv không tồn tại => thông báo
        // Ta duyệt trước qua masterData để xem mã này có tồn tại không
        boolean tonTai = false;
        for (NhanSu ns : masterData) {
            // Dùng 'contains' để khớp với logic lọc bên dưới
            if (ns.getMaNV().toLowerCase().contains(maNV)) {
                tonTai = true;
                break; // Tìm thấy 1 người khớp là đủ
            }
        }
        
        if (!tonTai) {
            // Nếu không tìm thấy ai, thông báo và dừng lại
            canhbao.thongbao("Không tồn tại", "Không tồn tại nhân viên hợp lệ.");
            return; //không tìm kiếm nữa
        }
        
        // Tạo một danh sách rỗng để lưu kết quả
        ObservableList<NhanSu> ketQuaTimKiem = FXCollections.observableArrayList();

        // Duyệt qua từng nhân sự trong danh sách gốc
        for (NhanSu ns : masterData) {
            boolean khop = true;
            if (!maNV.isEmpty() && !ns.getMaNV().toLowerCase().contains(maNV)) khop = false;
            if (khop && !hoTen.isEmpty() && !ns.getHoTen().toLowerCase().contains(hoTen)) khop = false;
            if (khop && ns.getCccd() != null && !cccd.isEmpty() && !ns.getCccd().toLowerCase().contains(cccd)) khop = false;           
            if (khop && ns.getEmail() != null && !email.isEmpty() && !ns.getEmail().toLowerCase().contains(email)) khop = false;            
            if (khop && ns.getSdt() != null && !sdt.isEmpty() && !ns.getSdt().toLowerCase().contains(sdt)) khop = false;            
            if (khop && gioiTinh != null && !gioiTinh.isEmpty() && !gioiTinh.equals(ns.getGioiTinh())) khop = false;          
            if (khop && ngaySinh != null && !ngaySinh.equals(ns.getNgaySinh())) khop = false;
            if (khop && maPB != null && !maPB.isEmpty() && !maPB.equals(ns.getMaPhongBan())) khop = false;           
            if (khop && chucVu != null && !chucVu.isEmpty() && !chucVu.equals(ns.getChucVu())) khop = false;           
            if (khop) {
                ketQuaTimKiem.add(ns); 
            }
        }
        timkiemnhansu_tbnhansu.setItems(ketQuaTimKiem);

        

        // Khóa các ô lại SAU KHI tìm kiếm
        timkiemnhansu_txma.setEditable(false);
        timkiemnhansu_txten.setEditable(false);
        timkiemnhansu_txcccd.setEditable(false);
        timkiemnhansu_txemail.setEditable(false);
        timkiemnhansu_txsdt.setEditable(false);
        
        // Khóa ComboBox và DatePicker (theo yêu cầu: không mờ)
        timkiemnhansu_cbogioitinh.setMouseTransparent(true);
        timkiemnhansu_cbogioitinh.setFocusTraversable(false);

        timkiemnhansu_datengaysinh.setMouseTransparent(true);
        timkiemnhansu_datengaysinh.setFocusTraversable(false);
        
        timkiemnhansu_cbmaPB.setMouseTransparent(true);
        timkiemnhansu_cbmaPB.setFocusTraversable(false);

        timkiemnhansu_cbchucvu.setMouseTransparent(true);
        timkiemnhansu_cbchucvu.setFocusTraversable(false);

        // Vô hiệu hóa nút "Tìm kiếm" (vì đã tìm xong)
        timkiemnhansu_bttimkiem.setDisable(true);


        if (ketQuaTimKiem.size() == 1) {
            // Nếu chỉ có 1 kết quả, tự động chọn hàng đó
            timkiemnhansu_tbnhansu.getSelectionModel().selectFirst();
            // Listener trong initialize() sẽ tự động được gọi và điền thông tin
        } else {
            // Nếu 0 hoặc nhiều hơn 1 kết quả, xóa lựa chọn
            timkiemnhansu_tbnhansu.getSelectionModel().clearSelection();
        }
    }
*/
        
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

        // Truyền lại dữ liệu vào controller (nếu cần)
        controller.initialize();

        // Chuyển scene
        timkiemnhansu_bttrolai.getScene().setRoot(root);
    }
}
