/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author lagia
 */
public class TimKiemPhongBan {
    // Khai báo cho các ô nhập liệu (TextField)
    @FXML private TextField timkiemphongban_txma;
    @FXML private TextField timkiemphongban_txten;
    @FXML private TextField timkiemphongban_txmaTP;
    @FXML private TextField timkiemphongban_txsdt;
    @FXML private TextField timkiemphongban_txemail;
    @FXML private TextField timkiemphongban_txtong;

    // Khai báo cho các nút bấm (Button)
    @FXML private Button timkiemphongban_bttimkiem;
    @FXML private Button timkiemphongban_bttrolai;

    // Khai báo cho Bảng (TableView) và các Cột (TableColumn)
    @FXML private TableView<PhongBan> timkiemphongban_tbphongban;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colma;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colten;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colmaTP;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colsdt;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colemail;
    @FXML private TableColumn<PhongBan, Integer> timkiemphongban_coltong;

    // Danh sách gốc, chứa tất cả phòng ban được truyền từ màn hình chính
    private ObservableList<PhongBan> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Chỉ cho mỗi cột biết nó sẽ lấy dữ liệu từ thuộc tính nào của lớp PhongBan
        timkiemphongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        timkiemphongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
        timkiemphongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
        timkiemphongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdtPhong"));
        timkiemphongban_colemail.setCellValueFactory(new PropertyValueFactory<>("emailPhong"));
        timkiemphongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));
    }
    
    public void setData(ObservableList<PhongBan> allPhongBan) {
        this.masterData.setAll(allPhongBan); // Sao chép toàn bộ dữ liệu vào danh sách gốc
        timkiemphongban_tbphongban.setItems(masterData); // Hiển thị tất cả lên bảng lúc đầu
    }

    @FXML
    private void timkiemphongban_timkiemAction() {
        // Lấy các giá trị tìm kiếm từ các ô TextField
        String ma = timkiemphongban_txma.getText().trim();
        String ten = timkiemphongban_txten.getText().trim();
        String maTP = timkiemphongban_txmaTP.getText().trim();
        String sdt = timkiemphongban_txsdt.getText().trim();
        String email = timkiemphongban_txemail.getText().trim();
        String tong = timkiemphongban_txtong.getText().trim(); // <-- THÊM MỚI

        // Tạo một danh sách mới để chứa kết quả tìm kiếm
        ObservableList<PhongBan> ketQuaTimKiem = FXCollections.observableArrayList();

        // Dùng vòng lặp "for-each" để duyệt qua từng phòng ban trong danh sách gốc (masterData)
        for (PhongBan pb : masterData) {
            boolean khopTatCa = true; // Bắt đầu bằng việc giả định phòng ban này khớp

            // 1. Kiểm tra Mã phòng
            if (khopTatCa && !ma.isEmpty()) {
                if (!pb.getMaPhong().toLowerCase().contains(ma.toLowerCase())) {
                    khopTatCa = false;
                }
            }

            // 2. Kiểm tra Tên phòng
            if (khopTatCa && !ten.isEmpty()) {
                if (!pb.getTenPhong().toLowerCase().contains(ten.toLowerCase())) {
                    khopTatCa = false;
                }
            }

            // 3. Kiểm tra Mã Trưởng phòng
            if (khopTatCa && !maTP.isEmpty()) {
                if (pb.getMaTruongPhong() == null || !pb.getMaTruongPhong().toLowerCase().contains(maTP.toLowerCase())) {
                    khopTatCa = false;
                }
            }

            // 4. Kiểm tra Số điện thoại
            if (khopTatCa && !sdt.isEmpty()) {
                if (pb.getSdtPhong() == null || !pb.getSdtPhong().toLowerCase().contains(sdt.toLowerCase())) {
                    khopTatCa = false;
                }
            }

            // 5. Kiểm tra Email
            if (khopTatCa && !email.isEmpty()) {
                if (pb.getEmailPhong() == null || !pb.getEmailPhong().toLowerCase().contains(email.toLowerCase())) {
                    khopTatCa = false;
                }
            }

            // 6. Kiểm tra Tổng số nhân viên (SỬA ĐỔI LOGIC)
            if (khopTatCa && !tong.isEmpty()) {
                try {
                    // Chuyển đổi chuỗi người dùng nhập thành số nguyên
                    int soNhanVienCanTim = Integer.parseInt(tong);
                    // So sánh chính xác với tổng số nhân viên của phòng ban
                    if (pb.getTongSoNhanVien() != soNhanVienCanTim) {
                        khopTatCa = false;
                    }
                } catch (NumberFormatException e) {
                    // Nếu người dùng nhập không phải là số (ví dụ: "abc"), 
                    // coi như không khớp với bất kỳ phòng ban nào.
                    khopTatCa = false;
                }
            }

            // Nếu sau tất cả các kiểm tra, phòng ban vẫn khớp -> thêm vào kết quả
            if (khopTatCa) {
                ketQuaTimKiem.add(pb); 
            }
        }
        
        // Cập nhật lại bảng để chỉ hiển thị danh sách kết quả đã lọc được
        timkiemphongban_tbphongban.setItems(ketQuaTimKiem);
    }

    @FXML
    private void timkiemphongban_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "Bạn đang thoát chức năng tìm kiếm");
        App.setRoot("phongban");
    }
}
