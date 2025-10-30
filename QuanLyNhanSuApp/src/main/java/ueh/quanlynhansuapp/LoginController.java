/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtMaNV;

    @FXML
    private PasswordField txtMatKhau;

    private final DataService dataService = DataService.getInstance();

    @FXML
    private void handleLogin() {
        String maNV = txtMaNV.getText().trim();
        String matKhau = txtMatKhau.getText().trim();

        if (maNV.isEmpty() || matKhau.isEmpty()) {
            showAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Mã NV và Mật khẩu.", Alert.AlertType.WARNING);
            return;
        }

        NhanSu ns = dataService.timNhanSuTheoMa(maNV);

        if (ns == null) {
            showAlert("Không tìm thấy", "Không tồn tại nhân viên có mã: " + maNV, Alert.AlertType.ERROR);
            return;
        }

        if (!matKhau.equals(ns.getMatKhau())) {
            showAlert("Sai mật khẩu", "Mật khẩu không đúng, vui lòng thử lại.", Alert.AlertType.ERROR);
            return;
        }

        // ✅ Đăng nhập thành công
        try {
            FXMLLoader loader;

            // 👉 Xác định FXML dựa vào chức vụ
            String chucVu = ns.getChucVu().toLowerCase();
            if (chucVu.contains("trưởng phòng") || chucVu.contains("quản lý")) {
                loader = new FXMLLoader(App.class.getResource("main.fxml"));
            } else {
                loader = new FXMLLoader(App.class.getResource("NVmain.fxml"));
            }

            Scene scene = new Scene(loader.load());

            // Nếu controller có hàm setCurrentUser thì truyền thông tin người đăng nhập
            Object controller = loader.getController();
            try {
                controller.getClass().getMethod("setCurrentUser", NhanSu.class).invoke(controller, ns);
            } catch (Exception ignore) {
                // Nếu controller không có hàm setCurrentUser thì bỏ qua
            }

            Stage currentStage = (Stage) txtMaNV.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            showAlert("Đăng nhập thành công",
                    "Xin chào, " + ns.getHoTen() + " (" + ns.getChucVu() + ")!",
                    Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải giao diện sau đăng nhập.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
