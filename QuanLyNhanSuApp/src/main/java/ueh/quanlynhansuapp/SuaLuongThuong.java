/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SuaLuongThuong {

    // ====== Các TextField trong FXML ======
    @FXML private TextField sualuongthuong_txmaluong;
    @FXML private TextField sualuongthuong_txmaNV;
    @FXML private TextField sualuongthuong_txthangnam;
    @FXML private TextField sualuongthuong_txluongcoban;
    @FXML private TextField sualuongthuong_txphucap;
    @FXML private TextField sualuongthuong_txthuong;
    @FXML private TextField sualuongthuong_txkhautru;
    @FXML private TextField sualuongthuong_txtongluong;
    @FXML private TextField sualuongthuong_txngaychitra;

    private LuongThuong luongThuong;

    /** Truyền dữ liệu khi người dùng bấm “Sửa” ở bảng chính */
    public void setLuongThuong(LuongThuong lt) {
        this.luongThuong = lt;
        sualuongthuong_txmaluong.setText(lt.getMaLuong());
        sualuongthuong_txmaNV.setText(lt.getMaNhanVien());
        sualuongthuong_txthangnam.setText(lt.getThangNam());
        sualuongthuong_txluongcoban.setText(String.valueOf(lt.getLuongCoBan()));
        sualuongthuong_txphucap.setText(String.valueOf(lt.getPhuCap()));
        sualuongthuong_txthuong.setText(String.valueOf(lt.getThuong()));
        sualuongthuong_txkhautru.setText(String.valueOf(lt.getKhauTru()));
        sualuongthuong_txtongluong.setText(String.valueOf(lt.getTongLuong()));
        sualuongthuong_txngaychitra.setText(lt.getNgayChiTra());
    }

    /** Khi nhấn nút “Sửa” */
    @FXML
    private void sualuongthuong_suaAction() {
        try {
            String maLuong = sualuongthuong_txmaluong.getText().trim();
            String maNV = sualuongthuong_txmaNV.getText().trim();
            String thangNam = sualuongthuong_txthangnam.getText().trim();
            double luongCoBan = Double.parseDouble(sualuongthuong_txluongcoban.getText());
            double phuCap = Double.parseDouble(sualuongthuong_txphucap.getText());
            double thuong = Double.parseDouble(sualuongthuong_txthuong.getText());
            double khauTru = Double.parseDouble(sualuongthuong_txkhautru.getText());
            String ngayChiTra = sualuongthuong_txngaychitra.getText().trim();

            if (maLuong.isEmpty() || maNV.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin!").showAndWait();
                return;
            }

            LuongThuong lt = new LuongThuong(maLuong, maNV, thangNam, luongCoBan, phuCap, thuong, khauTru, ngayChiTra);

            if (luongThuong == null) {
                DataService.getInstance().addLuongThuong(lt);
            } else {
                DataService.getInstance().updateLuongThuong(lt);
            }

            ((Stage) sualuongthuong_txmaluong.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Lỗi: Vui lòng nhập đúng định dạng số!").showAndWait();
        }
    }

    /** Khi nhấn nút “Quay lại” */
    @FXML
    private void sualuongthuong_quaylaiAction() {
        ((Stage) sualuongthuong_txmaluong.getScene().getWindow()).close();
    }
}
