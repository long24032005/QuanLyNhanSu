/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/*
    Controller cho giao diện "Tìm kiếm lương thưởng", phân quyền Admin (timkiemluongthuong.fxml)
    Chức năng:
    - Cho phép nhập mã nhân viên để lọc danh sách lương thưởng
    - Hiển thị kết quả trong bảng
    - Bao gồm xử lý ngoại lệ và thông báo rõ ràng cho người dùng
*/
public class TimKiemLuongThuong {

    // Định dạng ngày dùng chung
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private TextField timkiemluongthuong_txmaNV;
    @FXML private Button timkiemluongthuong_bttimkiem;
    @FXML private Button timkiemluongthuong_btquaylai;
    @FXML private TableView<LuongThuong> timkiemluongthuong_tbluongthuong;
    @FXML private TableColumn<LuongThuong, String> timkiemluongthuong_colmaluong;
    @FXML private TableColumn<LuongThuong, String> timkiemluongthuong_colmaNV;
    @FXML private TableColumn<LuongThuong, String> timkiemluongthuong_colthangnam;
    @FXML private TableColumn<LuongThuong, Double> timkiemluongthuong_colluongcoban;
    @FXML private TableColumn<LuongThuong, Double> timkiemluongthuong_colphucap;
    @FXML private TableColumn<LuongThuong, Double> timkiemluongthuong_colthuong;
    @FXML private TableColumn<LuongThuong, Double> timkiemluongthuong_colkhautru;
    @FXML private TableColumn<LuongThuong, Double> timkiemluongthuong_coltongluong;
    @FXML private TableColumn<LuongThuong, String> timkiemluongthuong_colngaychitra;

    // Danh sách dữ liệu lương thưởng để hiển thị
    private final ObservableList<LuongThuong> dsLuong = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Liên kết dữ liệu giữa đối tượng và cột
            timkiemluongthuong_colmaluong.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaLuong()));
            timkiemluongthuong_colmaNV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaNhanVien()));
            timkiemluongthuong_colthangnam.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getThangNam()));
            timkiemluongthuong_colngaychitra.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNgayChiTra()));

            timkiemluongthuong_colluongcoban.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
            timkiemluongthuong_colphucap.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPhuCap()));
            timkiemluongthuong_colthuong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getThuong()));
            timkiemluongthuong_colkhautru.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getKhauTru()));
            timkiemluongthuong_coltongluong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTongLuong()));

            // Căn trái cho các cột số
            alignLeft(timkiemluongthuong_colluongcoban);
            alignLeft(timkiemluongthuong_colphucap);
            alignLeft(timkiemluongthuong_colthuong);
            alignLeft(timkiemluongthuong_colkhautru);
            alignLeft(timkiemluongthuong_coltongluong);
            
            // Gán cho bảng này một danh sách trống
            timkiemluongthuong_tbluongthuong.setItems(FXCollections.observableArrayList());

        } catch (Exception e) {
            canhbao.canhbao("Lỗi khởi tạo", "Không thể khởi tạo bảng dữ liệu: " + e.getMessage());
        }
    }

    // Hàm định dạng hiển thị số và căn trái nội dung cột
    private void alignLeft(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : formatDouble(item));
                setStyle("-fx-alignment: CENTER-LEFT;");
            }
        });
    }

    // Hiển thị số không có phần thập phân dư thừa
    private String formatDouble(double val) {
        if (Math.abs(val - Math.rint(val)) < 1e-9) return String.valueOf((long)Math.rint(val));
        return String.format("%.2f", val);
    }

    // Nút "Tìm kiếm"
    @FXML
    private void timkiemluongthuong_timkiemAction() {
        try {
            String keyword = timkiemluongthuong_txmaNV.getText();
            
            // kiểm tra mã nhân viên có được nhập không
            if (keyword == null || keyword.trim().isEmpty()) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập mã nhân viên cần tìm!");
                return;
            }

            String maNV = keyword.trim().toUpperCase();

            // lọc danh sách theo mã nhân viên
            List<LuongThuong> ketQua = DataService.getInstance().getDsLuongThuong()
                    .stream()
                    .filter(l -> l.getMaNhanVien().equalsIgnoreCase(maNV))
                    .collect(Collectors.toList());

            // nếu không tìm thấy kết quả
            if (ketQua.isEmpty()) {
                canhbao.thongbao("Không tìm thấy", "Không có dữ liệu lương thưởng cho nhân viên: " + maNV);
                timkiemluongthuong_tbluongthuong.getItems().clear();
                return;
            }

            // cập nhật bảng với kết quả tìm được
            dsLuong.setAll(ketQua);
            timkiemluongthuong_tbluongthuong.setItems(dsLuong);
            canhbao.thongbao("Thành công", "Tìm thấy " + ketQua.size() + " bản ghi của nhân viên " + maNV);

        } catch (Exception e) {
            canhbao.canhbao("Lỗi tìm kiếm", "Không thể thực hiện tìm kiếm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Nút "Quay lại"
    @FXML
    private void timkiemluongthuong_quaylaiAction() {
        try {
            ((Stage) timkiemluongthuong_btquaylai.getScene().getWindow()).close();
        } catch (Exception e) {
            canhbao.canhbao("Lỗi thoát", "Không thể đóng cửa sổ: " + e.getMessage());
        }
    }
}

