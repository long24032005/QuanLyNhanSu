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
    @FXML private ComboBox<String> timkiemnhansu_cbmaPB;
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
        timkiemnhansu_cbchucvu.setItems(FXCollections.observableArrayList("Nhân viên", "Trưởng phòng", "Phó phòng", "Thực tập"));
    }

    public void setData(ObservableList<NhanSu> allNhanSu, ObservableList<String> dsMaPhong) {
        // Sao chép dữ liệu vào danh sách gốc của màn hình này
        this.masterData.setAll(allNhanSu);
        
        // Hiển thị toàn bộ nhân sự lên bảng lúc ban đầu
        timkiemnhansu_tbnhansu.setItems(masterData);
        
        // Nạp danh sách mã phòng ban vào ComboBox
        timkiemnhansu_cbmaPB.setItems(dsMaPhong);
    }

    @FXML
    private void timkiemnhansu_timkiemAction() {
        // Lấy tất cả các tiêu chí tìm kiếm từ người dùng
        String maNV = timkiemnhansu_txma.getText().trim().toLowerCase();
        String hoTen = timkiemnhansu_txten.getText().trim().toLowerCase();
        String cccd = timkiemnhansu_txcccd.getText().trim().toLowerCase();
        String email = timkiemnhansu_txemail.getText().trim().toLowerCase();
        String sdt = timkiemnhansu_txsdt.getText().trim().toLowerCase();
        String gioiTinh = timkiemnhansu_cbogioitinh.getValue();
        LocalDate ngaySinh = timkiemnhansu_datengaysinh.getValue();
        String maPB = timkiemnhansu_cbmaPB.getValue();
        String chucVu = timkiemnhansu_cbchucvu.getValue();
        
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
    }

    @FXML
    private void timkiemnhansu_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "Bạn đang thoát chức năng tìm kiếm");
        App.setRoot("nhansu"); // Quay về màn hình chính
    }
}
