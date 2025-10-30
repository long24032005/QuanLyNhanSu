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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // <<< THÊM VÀO
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NVMainController {


    @FXML
    private Button NVmain_btDangXuat;


    private NhanSu currentUser;

    // Phương thức này sẽ được gọi từ LoginController (Giữ nguyên)
    public void setCurrentUser(NhanSu user) {
        this.currentUser = user;
        System.out.println("Chào mừng nhân viên: " + currentUser.getHoTen());
    }

    // Phương thức xử lý nút "Thông tin nhân viên" (Giữ nguyên)
    @FXML
    private void NVmain_nhansuAction() throws IOException {
        if (currentUser == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NVnhansu.fxml"));
        Parent root = loader.load();
        NVNhanSuController controller = loader.getController();
        controller.displayNhanSuInfo(currentUser);

        Stage stage = new Stage();
        stage.setTitle("Thông tin cá nhân - " + currentUser.getHoTen());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // Phương thức xử lý nút "Thông tin lương thưởng" (Giữ nguyên)
    @FXML
    private void NVmain_luongthuongAction() throws IOException {
        if (currentUser == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NVluongthuong.fxml"));
        Parent root = loader.load();
        NVLuongThuongController controller = loader.getController();
        controller.loadLuongData(currentUser);

        Stage stage = new Stage();
        stage.setTitle("Bảng lương - " + currentUser.getHoTen());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // <<< THÊM VÀO: Phương thức xử lý cho nút Đăng xuất >>>
    @FXML
    void NVmain_dangXuatAction() {
        try {
            // 1. Lấy Stage (cửa sổ) hiện tại và đóng nó lại
            Stage currentStage = (Stage) NVmain_btDangXuat.getScene().getWindow();
            currentStage.close();

            // 2. Tải và hiển thị lại cửa sổ đăng nhập
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage loginStage = new Stage();
            loginStage.setTitle("Đăng nhập");
            loginStage.setScene(scene);
            loginStage.show();

        } catch (IOException e) {
        }
    }
}

