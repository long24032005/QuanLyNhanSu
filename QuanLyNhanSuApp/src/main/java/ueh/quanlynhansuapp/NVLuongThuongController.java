/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

/*
Controller cho giao diện lương thưởng của nhân viên (NVluongthuong.fxml).
Chỉ hiển thị danh sách lương của chính người đang đăng nhập. Nhân viên không thể chỉnh sửa, thêm, hay xóa dữ liệu.
*/
public class NVLuongThuongController {
    // Bảng và các cột hiển thị thông tin lương thưởng
    @FXML private TableView<LuongThuong> NVluongthuong_tbluongthuong;
    @FXML private TableColumn<LuongThuong, String> NVluongthuong_colmaluong;
    @FXML private TableColumn<LuongThuong, String> NVluongthuong_colmaNV;
    @FXML private TableColumn<LuongThuong, String> NVluongthuong_colthangnam;
    @FXML private TableColumn<LuongThuong, Double> NVluongthuong_colluongcoban;
    @FXML private TableColumn<LuongThuong, Double> NVluongthuong_colphucap;
    @FXML private TableColumn<LuongThuong, Double> NVluongthuong_colthuong;
    @FXML private TableColumn<LuongThuong, Double> NVluongthuong_colkhautru;
    @FXML private TableColumn<LuongThuong, Double> NVluongthuong_coltongluong;
    @FXML private TableColumn<LuongThuong, String> NVluongthuong_colngaychitra;
    @FXML private Button NVluongthuong_btquaylai;

     // Lấy dữ liệu qua DataService
    private final DataService dataService = DataService.getInstance();

    // Hàm hiển thị ds lương của nhân viên
    public void loadLuongData(NhanSu user) {
        if (user == null) return; // Nếu chưa truyền người dùng => thoát luôn

        // Lấy toàn bộ bản ghi lương ứng với mã nhân viên hiện tại
        ObservableList<LuongThuong> luongList = dataService.getLuongByMaNV(user.getMaNV());
        System.out.println("[DEBUG] Load " + luongList.size() + " bản ghi lương của NV: " + user.getMaNV());

        // Gắn dữ liệu vào từng cột trong bảng
        NVluongthuong_colmaluong.setCellValueFactory(new PropertyValueFactory<>("maLuong"));
        NVluongthuong_colmaNV.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        NVluongthuong_colthangnam.setCellValueFactory(new PropertyValueFactory<>("thangNam"));
        NVluongthuong_colngaychitra.setCellValueFactory(new PropertyValueFactory<>("ngayChiTra"));

        // Các cột số (double)
        NVluongthuong_colluongcoban.setCellValueFactory(new PropertyValueFactory<>("luongCoBan"));
        NVluongthuong_colphucap.setCellValueFactory(new PropertyValueFactory<>("phuCap"));
        NVluongthuong_colthuong.setCellValueFactory(new PropertyValueFactory<>("thuong"));
        NVluongthuong_colkhautru.setCellValueFactory(new PropertyValueFactory<>("khauTru"));
        NVluongthuong_coltongluong.setCellValueFactory(new PropertyValueFactory<>("tongLuong"));

        //Định dạng lại các cột số để căn trái và tránh hiển thị E7
        alignLeft(NVluongthuong_colluongcoban);
        alignLeft(NVluongthuong_colphucap);
        alignLeft(NVluongthuong_colthuong);
        alignLeft(NVluongthuong_colkhautru);
        alignLeft(NVluongthuong_coltongluong);

        // Gán danh sách vào bảng và cho bảng tự co giãn cột
        NVluongthuong_tbluongthuong.setItems(luongList);
        NVluongthuong_tbluongthuong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Hàm định dạng double thành số bình thường 
    private String doubleToPlain(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9) {
            return String.valueOf((long)Math.rint(v)); // Làm tròn nếu là số nguyên
        }
        return String.format("%.2f", v); // Giữ 2 số thập phân nếu có
    }

    // Hàm căn trái và format các cột số
    private void alignLeft(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : doubleToPlain(item));
                setStyle("-fx-alignment: CENTER-LEFT;");
            }
        });
    }

    // Nút quay lại 
    @FXML
    private void NVluongthuong_quaylaiAction() {
        // Lấy Stage (cửa sổ) hiện tại và đóng lại để quay về màn hình trước
        Stage stage = (Stage) NVluongthuong_btquaylai.getScene().getWindow();
        stage.close();
    }
}

