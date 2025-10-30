/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class First {

    private NhanSu currentUser;

    @FXML
    private Button main_btnhansu;
    @FXML
    private Button main_btphongban;
    @FXML
    private Button main_btluongthuong;
    @FXML
    private Button main_btDangXuat;

    // Nút mở màn hình Nhân sự
    @FXML
    private void main_nhansuAction() throws IOException {
        if (isEmployee()) {
            showAccessDenied();
            return;
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("nhansu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) main_btnhansu.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // Nút mở màn hình Phòng ban
    @FXML
    private void main_phongbanAction() throws IOException {
        if (isEmployee()) {
            showAccessDenied();
            return;
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("phongban.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) main_btphongban.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // Nút mở màn hình Lương thưởng
    @FXML
    private void main_luongthuongAction() throws IOException {
        if (isEmployee()) {
            showAccessDenied();
            return;
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("luongthuong.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) main_btluongthuong.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
    // Nút Đăng xuất
    @FXML
    void main_dangXuatAction() {
        try {
            Stage currentStage = (Stage) main_btDangXuat.getScene().getWindow();
            currentStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage loginStage = new Stage();
            loginStage.setTitle("Đăng nhập");
            loginStage.setScene(scene);
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dùng để truyền dữ liệu người đăng nhập từ LoginController
    public void setCurrentUser(NhanSu user) {
        this.currentUser = user;
        System.out.println("Người đăng nhập: " + user.getHoTen() + " - " + user.getChucVu());

        // 🔹 Nếu là nhân viên thường thì vô hiệu hóa các chức năng quản lý
        if (isEmployee()) {
            if (main_btnhansu != null) main_btnhansu.setDisable(true);
            if (main_btphongban != null) main_btphongban.setDisable(true);
            if (main_btluongthuong != null) main_btluongthuong.setDisable(true);
            if (main_btDangXuat != null) main_btDangXuat.setDisable(false);

            showAccessDenied();
        }
    }

    // Kiểm tra người dùng có phải nhân viên không
    private boolean isEmployee() {
        if (currentUser == null) return false;
        String role = currentUser.getChucVu();
        if (role == null) return true;
        role = role.toLowerCase();
        return !(role.contains("manager") || role.contains("trưởng") || role.contains("admin"));
    }

    // Hiển thị cảnh báo khi nhân viên truy cập vào khu vực quản lý
    private void showAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Không đủ quyền truy cập");
        alert.setContentText("Chức năng này chỉ dành cho Quản lý / Trưởng phòng.");
        alert.showAndWait();
    }
}