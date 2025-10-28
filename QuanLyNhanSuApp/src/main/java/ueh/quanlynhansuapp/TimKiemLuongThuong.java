/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */

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

/**
 * Controller cho màn hình "Tìm kiếm lương thưởng"
 * - Cho phép nhập mã nhân viên → lọc danh sách lương thưởng theo mã NV
 * - Bao phủ ngoại lệ, đảm bảo ổn định và dễ đọc
 */
public class TimKiemLuongThuong {

    // ====== Định dạng ngày dùng chung ======
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ====== FXML Elements ======
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

    private final ObservableList<LuongThuong> dsLuong = FXCollections.observableArrayList();

    // ====================== KHỞI TẠO ======================
    @FXML
    public void initialize() {
        try {
            // === Cột chuỗi ===
            timkiemluongthuong_colmaluong.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaLuong()));
            timkiemluongthuong_colmaNV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaNhanVien()));
            timkiemluongthuong_colthangnam.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getThangNam()));
            timkiemluongthuong_colngaychitra.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNgayChiTra()));

            // === Cột số ===
            timkiemluongthuong_colluongcoban.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
            timkiemluongthuong_colphucap.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPhuCap()));
            timkiemluongthuong_colthuong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getThuong()));
            timkiemluongthuong_colkhautru.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getKhauTru()));
            timkiemluongthuong_coltongluong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTongLuong()));

            // === Căn trái toàn bộ (đồng bộ phong cách) ===
            alignLeft(timkiemluongthuong_colluongcoban);
            alignLeft(timkiemluongthuong_colphucap);
            alignLeft(timkiemluongthuong_colthuong);
            alignLeft(timkiemluongthuong_colkhautru);
            alignLeft(timkiemluongthuong_coltongluong);

            // === Load data mặc định ===
            DataService.getInstance().reloadAllData();
            dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
            timkiemluongthuong_tbluongthuong.setItems(dsLuong);

        } catch (Exception e) {
            canhbao.canhbao("Lỗi khởi tạo", "Không thể khởi tạo bảng dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ====================== CĂN LỀ TRÁI CHO CỘT SỐ ======================
    private void alignLeft(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : formatDouble(item));
                setStyle("-fx-alignment: CENTER-LEFT;");
            }
        });
    }

    private String formatDouble(double val) {
        if (Math.abs(val - Math.rint(val)) < 1e-9) return String.valueOf((long)Math.rint(val));
        return String.format("%.2f", val);
    }

    // ====================== CHỨC NĂNG TÌM KIẾM ======================
    @FXML
    private void timkiemluongthuong_timkiemAction() {
        try {
            String keyword = timkiemluongthuong_txmaNV.getText();
            if (keyword == null || keyword.trim().isEmpty()) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập mã nhân viên cần tìm!");
                return;
            }

            String maNV = keyword.trim().toUpperCase();

            List<LuongThuong> ketQua = DataService.getInstance().getDsLuongThuong()
                    .stream()
                    .filter(l -> l.getMaNhanVien().equalsIgnoreCase(maNV))
                    .collect(Collectors.toList());

            if (ketQua.isEmpty()) {
                canhbao.thongbao("Không tìm thấy", "Không có dữ liệu lương thưởng cho nhân viên: " + maNV);
                timkiemluongthuong_tbluongthuong.getItems().clear();
                return;
            }

            dsLuong.setAll(ketQua);
            timkiemluongthuong_tbluongthuong.setItems(dsLuong);
            canhbao.thongbao("Thành công", "Tìm thấy " + ketQua.size() + " bản ghi của nhân viên " + maNV);

        } catch (Exception e) {
            canhbao.canhbao("Lỗi tìm kiếm", "Không thể thực hiện tìm kiếm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ====================== QUAY LẠI ======================
    @FXML
    private void timkiemluongthuong_quaylaiAction() {
        try {
            ((Stage) timkiemluongthuong_btquaylai.getScene().getWindow()).close();
        } catch (Exception e) {
            canhbao.canhbao("Lỗi thoát", "Không thể đóng cửa sổ: " + e.getMessage());
        }
    }
}

