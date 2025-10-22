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
        
        timkiemphongban_tbphongban.getSelectionModel().selectedItemProperty().addListener((obs, oldV, pb) -> {
            if (pb != null) {
                // Hiển thị dữ liệu của phòng ban (pb) lên các ô tìm kiếm
                timkiemphongban_txma.setText(pb.getMaPhong());
                timkiemphongban_txten.setText(pb.getTenPhong());
                timkiemphongban_txmaTP.setText(pb.getMaTruongPhong());
                timkiemphongban_txsdt.setText(pb.getSdtPhong());
                timkiemphongban_txemail.setText(pb.getEmailPhong());
                timkiemphongban_txtong.setText(String.valueOf(pb.getTongSoNhanVien()));
            }
        });
        timkiemphongban_txtong.setEditable(false);



        timkiemphongban_txten.setEditable(false);
        timkiemphongban_txmaTP.setEditable(false);
        timkiemphongban_txsdt.setEditable(false);
        timkiemphongban_txemail.setEditable(false);

    }
    
    public void setData(ObservableList<PhongBan> allPhongBan) {
        this.masterData.setAll(allPhongBan); // Sao chép toàn bộ dữ liệu vào danh sách gốc
        timkiemphongban_tbphongban.setItems(masterData); // Hiển thị tất cả lên bảng lúc đầu
    }

    @FXML
    private void timkiemphongban_timkiemAction() {
        // Lấy các giá trị tìm kiếm từ các ô TextField
        String maPB_input = timkiemphongban_txma.getText().trim();
        String ten = timkiemphongban_txten.getText().trim();
        String maTP = timkiemphongban_txmaTP.getText().trim();
        String sdt = timkiemphongban_txsdt.getText().trim();
        String email = timkiemphongban_txemail.getText().trim();
        String tong = timkiemphongban_txtong.getText().trim(); //
        
        // 1. Kiểm tra Bỏ trống mnv => warning
        if (maPB_input.isEmpty()) {
            // Sửa "mssv" thành "Mã nhân viên" cho chính xác
            canhbao.canhbao("Thiếu thông tin", "Mã Phòng Ban không được để trống!");
            return; // Dừng hàm, không tìm kiếm nữa
        }
        
        String maPB = maPB_input.toLowerCase();

        // 2. Kiểm tra Nhập mnv không tồn tại => thông báo
        // Ta duyệt trước qua masterData để xem mã này có tồn tại không
        boolean tonTai = false;
        for (PhongBan pb : masterData) {
            // Dùng 'contains' để khớp với logic lọc bên dưới
            if (pb.getMaPhong().toLowerCase().contains(maPB)) {
                tonTai = true;
                break; // Tìm thấy 1 người khớp là đủ
            }
        }
        
        if (!tonTai) {
            // Nếu không tìm thấy ai, thông báo và dừng lại
            canhbao.thongbao("Không tồn tại", "Không tồn tại Phòng Ban hợp lệ.");
            return; //không tìm kiếm nữa
        }
        
        // Tạo một danh sách mới để chứa kết quả tìm kiếm
        ObservableList<PhongBan> ketQuaTimKiem = FXCollections.observableArrayList();

        // Dùng vòng lặp "for-each" để duyệt qua từng phòng ban trong danh sách gốc (masterData)
        for (PhongBan pb : masterData) {
            boolean khopTatCa = true; // Bắt đầu bằng việc giả định phòng ban này khớp
            // 1. Kiểm tra Mã phòng
            if (khopTatCa && !maPB.isEmpty()) {
                if (!pb.getMaPhong().toLowerCase().contains(maPB.toLowerCase())) {
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

        timkiemphongban_txten.setEditable(false);
        timkiemphongban_txmaTP.setEditable(false);
        timkiemphongban_txsdt.setEditable(false);
        timkiemphongban_txemail.setEditable(false);




        if (ketQuaTimKiem.size() == 1) {
            // Nếu chỉ có 1 kết quả, tự động chọn hàng đó
            timkiemphongban_tbphongban.getSelectionModel().selectFirst();
            // Listener trong initialize() sẽ tự động được gọi và điền thông tin
        } else {
            // Nếu 0 hoặc nhiều hơn 1 kết quả, xóa lựa chọn
            timkiemphongban_tbphongban.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void timkiemphongban_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "Bạn đang thoát chức năng tìm kiếm");
        App.setRoot("phongban");
    }
}
