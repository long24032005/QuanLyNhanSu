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
/*
Màn hình đăng nhập của ứng dụng.
 - Người dùng nhập Mã NV và Mật khẩu, nếu hợp lệ sẽ chuyển sang giao diện chính.
 - Dựa theo chức vụ mà hệ thống mở giao diện phù hợp (admin / nhân viên).
 */
public class LoginController {

    @FXML
    private TextField txtMaNV;  // Ô nhập mã nhân viên

    @FXML
    private PasswordField txtMatKhau; // Ô nhập mật khẩu

    // Lớp trung gian để truy cập dữ liệu nhân sự trong bộ nhớ và database
    private final DataService dataService = DataService.getInstance();

    // Hàm xử lý khi người dùng bấm nút "Đăng nhập"
    @FXML
    private void handleLogin() {
        // Lấy dữ liệu người dùng nhập
        String maNV = txtMaNV.getText().trim();
        String matKhau = txtMatKhau.getText().trim();

        // Kiểm tra xem người dùng có bỏ trống không
        if (maNV.isEmpty() || matKhau.isEmpty()) {
            showAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Mã NV và Mật khẩu.", Alert.AlertType.WARNING);
            return;
        }

        // Tìm nhân viên theo mã trong danh sách hiện có
        NhanSu ns = dataService.timNhanSuTheoMa(maNV);

        // Nếu không tìm thấy mã nhân viên trong hệ thống
        if (ns == null) {
            showAlert("Không tìm thấy", "Không tồn tại nhân viên có mã: " + maNV, Alert.AlertType.ERROR);
            return;
        }

        // Nếu mật khẩu nhập sai
        if (!matKhau.equals(ns.getMatKhau())) {
            showAlert("Sai mật khẩu", "Mật khẩu không đúng, vui lòng thử lại.", Alert.AlertType.ERROR);
            return;
        }

        // Đăng nhập thành công
        try {
            FXMLLoader loader;

            // Xác định FXML dựa vào chức vụ
            String chucVu = ns.getChucVu().toLowerCase();
            // Trưởng phòng/Quản lý => giao diện main.fxml
            if (chucVu.contains("trưởng phòng") || chucVu.contains("quản lý")) {
                loader = new FXMLLoader(App.class.getResource("main.fxml"));
            // Nhân viên thường → NVmain.fxml (giao diện giới hạn chức năng)
            } else {
                loader = new FXMLLoader(App.class.getResource("NVmain.fxml"));
            }
            // Tải file FXML tương ứng
            Scene scene = new Scene(loader.load());

            // Lấy controller của màn hình mới
            Object controller = loader.getController();
            try {
                // Nếu controller có hàm setCurrentUser thì truyền thông tin người đăng nhập vào để màn hình sau biết ai đang dùng
                controller.getClass().getMethod("setCurrentUser", NhanSu.class).invoke(controller, ns);
            } catch (Exception ignore) {
                // Nếu controller không có hàm setCurrentUser thì bỏ qua
            }
            
            // Chuyển sang giao diện mới
            Stage currentStage = (Stage) txtMaNV.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();
            // Hiện thông báo đăng nhập thành công
            showAlert("Đăng nhập thành công",
                    "Xin chào, " + ns.getHoTen() + " (" + ns.getChucVu() + ")!",
                    Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải giao diện sau đăng nhập.", Alert.AlertType.ERROR);
        }
    }

    // Hàm hiển thị thông báo
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
