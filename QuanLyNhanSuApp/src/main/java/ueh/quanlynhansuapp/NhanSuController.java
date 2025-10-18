/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author lagia
 */
public class NhanSuController {
    // --- KHAI BÁO BIẾN CHO NHÂN SỰ ---
    @FXML private TextField nhansu_txma;
    @FXML private TextField nhansu_txten;
    @FXML private ComboBox<String> nhansu_cbogioitinh;
    @FXML private DatePicker nhansu_datengaysinh;
    @FXML private TextField nhansu_txcccd;
    @FXML private TextField nhansu_txemail;
    @FXML private TextField nhansu_txsdt;
    @FXML private ComboBox<String> nhansu_cbmaPB;
    @FXML private ComboBox<String> nhansu_cbchucvu;

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

    @FXML private Button nhansu_btthem;
    @FXML private Button nhansu_btxoa;
    @FXML private Button nhansu_btsua;
    @FXML private Button nhansu_bttimkiem;

    // Lấy instance của DataService để truy cập dữ liệu chung
    private final DataService dataService = DataService.getInstance();
    
    // Danh sách riêng cho ComboBox mã phòng ban
    private final ObservableList<String> dsMaPhongForCombo = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // --- CÀI ĐẶT CÁC CỘT CHO BẢNG NHÂN SỰ ---
        nhansu_colma.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        nhansu_colten.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        nhansu_colgioitinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        nhansu_colngaysinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        nhansu_colcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        nhansu_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        nhansu_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        nhansu_colmaPB.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
        nhansu_colchucvu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // --- GÁN NGUỒN DỮ LIỆU TỪ DATASERVICE ---
        nhansu_tbnhansu.setItems(dataService.getDsNhanSu());

        // --- CÀI ĐẶT GIÁ TRỊ CHO CÁC COMBOBOX ---
        nhansu_cbogioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        nhansu_cbchucvu.setItems(FXCollections.observableArrayList("Nhân viên", "Trưởng phòng", "Phó phòng", "Thực tập"));

        // Tải danh sách mã phòng ban vào ComboBox lần đầu
        reloadPhongBanToCombo();
        // Tự động cập nhật ComboBox khi danh sách phòng ban trong DataService thay đổi
        dataService.getDsPhongBan().addListener((ListChangeListener<PhongBan>) c -> reloadPhongBanToCombo());

        // --- SỰ KIỆN KHI CHỌN MỘT DÒNG TRONG BẢNG ---
        nhansu_tbnhansu.getSelectionModel().selectedItemProperty().addListener((obs, oldV, ns) -> {
            if (ns != null) {
                nhansu_txma.setText(ns.getMaNV());
                nhansu_txten.setText(ns.getHoTen());
                nhansu_cbogioitinh.setValue(ns.getGioiTinh());
                nhansu_datengaysinh.setValue(ns.getNgaySinh());
                nhansu_txcccd.setText(ns.getCccd());
                nhansu_txemail.setText(ns.getEmail());
                nhansu_txsdt.setText(ns.getSdt());
                nhansu_cbmaPB.setValue(ns.getMaPhongBan());
                nhansu_cbchucvu.setValue(ns.getChucVu());
            }
        });
    }
    
    @FXML
    private void nhansu_themAction() {
        String maNV = nhansu_txma.getText().trim();
        String hoTen = nhansu_txten.getText().trim();
        String gioiTinh = nhansu_cbogioitinh.getValue();
        LocalDate ngaySinh = nhansu_datengaysinh.getValue();
        String cccd = nhansu_txcccd.getText().trim();
        String email = nhansu_txemail.getText().trim();
        String sdt = nhansu_txsdt.getText().trim();
        String maPhongBan = nhansu_cbmaPB.getValue();
        String chucVu = nhansu_cbchucvu.getValue();

        if (maNV.isEmpty() || hoTen.isEmpty() || ngaySinh == null || maPhongBan == null) {
            canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập Mã NV, Họ tên, Ngày sinh và chọn Phòng ban.");
            return;
        }

        if (dataService.timNhanSuTheoMa(maNV) != null) {
            canhbao.canhbao("Trùng mã", "Mã nhân sự \"" + maNV + "\" đã tồn tại.");
            return;
        }
        
        NhanSu ns = new NhanSu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu);

        // Thêm thông qua DataService để đồng bộ
        if (dataService.addNhanSu(ns)) {
            canhbao.thongbao("Thành công", "Đã thêm nhân sự \"" + hoTen + "\".");
             // Xóa trắng các ô nhập liệu
            nhansu_txma.clear();
            nhansu_txten.clear();
            nhansu_cbogioitinh.getSelectionModel().clearSelection();
            nhansu_datengaysinh.setValue(null);
            nhansu_txcccd.clear();
            nhansu_txemail.clear();
            nhansu_txsdt.clear();
            nhansu_cbmaPB.getSelectionModel().clearSelection();
            nhansu_cbchucvu.getSelectionModel().clearSelection();
        }
    }

    @FXML 
    private void nhansu_xoaAction() { 
         NhanSu selectedNhanSu = nhansu_tbnhansu.getSelectionModel().getSelectedItem();
        if (selectedNhanSu == null) {
            canhbao.canhbao("Chưa chọn nhân viên", "Vui lòng chọn một nhân viên để xóa.");
            return;
        }
        
        boolean dongY = canhbao.xacNhan(
                    "Xác nhận xóa",
                    "Bạn có chắc chắn muốn xóa nhân viên: " + selectedNhanSu.getHoTen() + "?",
                    "Hành động này không thể hoàn tác."
        );
        
        if (dongY) {
            dataService.deleteNhanSu(selectedNhanSu);
            canhbao.thongbao("Thành công", "Đã xóa nhân viên.");
        }
    }
    
    @FXML 
    private void nhansu_suaAction() throws IOException {
        NhanSu nsselected = nhansu_tbnhansu.getSelectionModel().getSelectedItem();
        if(nsselected == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"Chọn 1 hàng để sửa", ButtonType.YES);
            a.setTitle("Thong Tin");
            a.showAndWait();
            return;
        }
         
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("suanhansu.fxml"));
            Parent root = loader.load();

            SuaNhanSu controller = loader.getController();
            controller.setData(nsselected);

            nhansu_btsua.getScene().setRoot(root);

        } catch (IOException e) {
            // In chi tiết lỗi ra Console để bạn xem
            e.printStackTrace(); 
        
            // Hiển thị một cảnh báo trực tiếp trên giao diện
            canhbao.canhbao("Lỗi Tải Giao Diện", 
                "Không thể tải file suanhansu.fxml.\n" +
                "Vui lòng kiểm tra cửa sổ Output/Console để xem chi tiết lỗi.");
        }
    }
    
    @FXML 
    private void nhansu_timkiemAction() throws IOException { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timkiemnhansu.fxml"));
        Parent root = loader.load();
    
        // Lấy controller của màn hình "timkiemnhansu"
        TimKiemNhanSu controller = loader.getController();
    
        // Lấy danh sách mã phòng ban từ dsMaPhongForCombo đã có sẵn
        // Và truyền toàn bộ danh sách nhân sự từ dataService
        controller.setData(dataService.getDsNhanSu(), dsMaPhongForCombo);
    
        // Lấy Scene hiện tại và set root mới
        nhansu_bttimkiem.getScene().setRoot(root);
    }
    
    private void reloadPhongBanToCombo() {
        dsMaPhongForCombo.clear();
        for (PhongBan pb : dataService.getDsPhongBan()) {
            dsMaPhongForCombo.add(pb.getMaPhong());
        }
        nhansu_cbmaPB.setItems(dsMaPhongForCombo);
    }
}
