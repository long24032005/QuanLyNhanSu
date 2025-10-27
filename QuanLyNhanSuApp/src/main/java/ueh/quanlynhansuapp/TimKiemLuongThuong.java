/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TimKiemLuongThuong {

    @FXML private TextField timkiemluongthuong_txmaNV;
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

    private ObservableList<LuongThuong> dsKetQua = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        timkiemluongthuong_colmaluong.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaLuong()));
        timkiemluongthuong_colmaNV.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaNhanVien()));
        timkiemluongthuong_colthangnam.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getThangNam()));
        timkiemluongthuong_colluongcoban.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
        timkiemluongthuong_colphucap.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPhuCap()));
        timkiemluongthuong_colthuong.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getThuong()));
        timkiemluongthuong_colkhautru.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getKhauTru()));
        timkiemluongthuong_coltongluong.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTongLuong()));
        timkiemluongthuong_colngaychitra.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNgayChiTra()));
    }

    /** Khi nhấn nút “Tìm kiếm” */
    @FXML
    private void timkiemluongthuong_timkiemAction() {
        String tuKhoa = timkiemluongthuong_txmaNV.getText().trim().toLowerCase();
        dsKetQua.clear();

        for (LuongThuong lt : DataService.getInstance().getDsLuongThuong()) {
            if (lt.getMaLuong().toLowerCase().contains(tuKhoa)
                || lt.getMaNhanVien().toLowerCase().contains(tuKhoa)
                || lt.getThangNam().toLowerCase().contains(tuKhoa)) {
                dsKetQua.add(lt);
            }
        }
        timkiemluongthuong_tbluongthuong.setItems(dsKetQua);
    }

    /** Khi nhấn nút “Quay lại” */
    @FXML
    private void timkiemluongthuong_quaylaiAction() {
        ((Stage) timkiemluongthuong_tbluongthuong.getScene().getWindow()).close();
    }
}

