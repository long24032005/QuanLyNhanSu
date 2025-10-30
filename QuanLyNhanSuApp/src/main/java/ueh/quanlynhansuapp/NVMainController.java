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
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


/* 
Controller của giao diện chính của nhân viên (NVmain.fxml)
*/
public class NVMainController {

    @FXML
    private Button NVmain_btDangXuat;


    private NhanSu currentUser;

    // Phương thức này sẽ được gọi từ LoginController sau khi login thành công để lưu lại thông tin người dùng cho các nút khác sử dụng
    public void setCurrentUser(NhanSu user) {
        this.currentUser = user;
        // Log kiểm tra nhanh đảm bảo đã nhận đúng người dùng
        System.out.println("Chào mừng nhân viên: " + currentUser.getHoTen());
    }

    // Nút "Thông tin nhân viên"
    @FXML
    private void NVmain_nhansuAction() throws IOException {
        if (currentUser == null) return; // Nếu chưa có user (lỗi luồng gọi) thì dừng
 
        // Tạo loader và nạp FXML giao diện thông tin nhân sự
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NVnhansu.fxml"));
        Parent root = loader.load();
        
        // Lấy controller của màn hình NVnhansu để truyền dữ liệu người dùng
        NVNhanSuController controller = loader.getController();
        controller.displayNhanSuInfo(currentUser); // Truyền user để màn hình con chủ động render đúng thông tin cá nhân

        //  Tạo Stage riêng cho cửa sổ thông tin cá nhân
        Stage stage = new Stage();
        stage.setTitle("Thông tin cá nhân - " + currentUser.getHoTen());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); //  Đặt modality = APPLICATION_MODAL để bắt buộc đóng cửa sổ con trước khi quay về, tránh người dùng mở nhiều popup chồng nhau
        stage.showAndWait();
    }

    // Nút "Thông tin lương thưởng"
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

    // Nút "Đăng xuất"
    @FXML
    void NVmain_dangXuatAction() {
        try {
            // Lấy Stage hiện tại và đóng nó lại
            Stage currentStage = (Stage) NVmain_btDangXuat.getScene().getWindow();
            currentStage.close();

            // Tải và hiển thị lại cửa sổ đăng nhập (login.fxml)
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

