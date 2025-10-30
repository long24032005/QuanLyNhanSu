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

public class NVLuongThuongController {

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

    private final DataService dataService = DataService.getInstance();

    /** 🔹 Load dữ liệu lương của nhân viên */
    public void loadLuongData(NhanSu user) {
        if (user == null) return;

        ObservableList<LuongThuong> luongList = dataService.getLuongByMaNV(user.getMaNV());
        System.out.println("[DEBUG] Load " + luongList.size() + " bản ghi lương của NV: " + user.getMaNV());

        NVluongthuong_colmaluong.setCellValueFactory(new PropertyValueFactory<>("maLuong"));
        NVluongthuong_colmaNV.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        NVluongthuong_colthangnam.setCellValueFactory(new PropertyValueFactory<>("thangNam"));
        NVluongthuong_colngaychitra.setCellValueFactory(new PropertyValueFactory<>("ngayChiTra"));

        // Gán giá trị double
        NVluongthuong_colluongcoban.setCellValueFactory(new PropertyValueFactory<>("luongCoBan"));
        NVluongthuong_colphucap.setCellValueFactory(new PropertyValueFactory<>("phuCap"));
        NVluongthuong_colthuong.setCellValueFactory(new PropertyValueFactory<>("thuong"));
        NVluongthuong_colkhautru.setCellValueFactory(new PropertyValueFactory<>("khauTru"));
        NVluongthuong_coltongluong.setCellValueFactory(new PropertyValueFactory<>("tongLuong"));

        // === Định dạng lại các cột số để tránh hiển thị E7 ===
        alignRight(NVluongthuong_colluongcoban);
        alignRight(NVluongthuong_colphucap);
        alignRight(NVluongthuong_colthuong);
        alignRight(NVluongthuong_colkhautru);
        alignRight(NVluongthuong_coltongluong);

        NVluongthuong_tbluongthuong.setItems(luongList);
        NVluongthuong_tbluongthuong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /** 🔹 Hàm định dạng double thành số bình thường (không E7) */
    private String doubleToPlain(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9) {
            return String.valueOf((long)Math.rint(v)); // Làm tròn nếu là số nguyên
        }
        return String.format("%.2f", v); // Giữ 2 số thập phân nếu có
    }

    /** 🔹 Căn phải và format các cột số */
    private void alignRight(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : doubleToPlain(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });
    }

    /** 🔹 Nút quay lại */
    @FXML
    private void NVluongthuong_quaylaiAction() {
        // Lấy Stage (cửa sổ) hiện tại và đóng nó
        Stage stage = (Stage) NVluongthuong_btquaylai.getScene().getWindow();
        stage.close();
    }
}

