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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*
Controller cho giao diện xem thông tin cá nhân của NHÂN VIÊN.
Màn hình này chỉ hiển thị thông tin, không cho phép chỉnh sửa.
 */
public class NVNhanSuController {

    @FXML private TextField NVnhansu_txma;
    @FXML private TextField NVnhansu_txten;
    @FXML private TextField NVnhansu_txgioitinh;
    @FXML private DatePicker NVnhansu_datengaysinh;
    @FXML private TextField NVnhansu_txcccd;
    @FXML private TextField NVnhansu_txemail;
    @FXML private TextField NVnhansu_txsdt;
    @FXML private TextField NVnhansu_txmaPB;
    @FXML private TextField NVnhansu_txchucvu;
    @FXML private Button NVnhansu_btquaylai;

     // Nhận đối tượng NhanSu từ NVMainController và hiển thị thông tin lên giao diện
    public void displayNhanSuInfo(NhanSu user) {
        if (user == null) return;

        NVnhansu_txma.setText(user.getMaNV());
        NVnhansu_txten.setText(user.getHoTen());
        NVnhansu_txgioitinh.setText(user.getGioiTinh());
        NVnhansu_datengaysinh.setValue(user.getNgaySinh());
        NVnhansu_txcccd.setText(user.getCccd()); 
        NVnhansu_txemail.setText(user.getEmail());
        NVnhansu_txsdt.setText(user.getSdt());
        NVnhansu_txmaPB.setText(user.getMaPhongBan()); 
        NVnhansu_txchucvu.setText(user.getChucVu());

        // Vô hiệu hóa tất cả các trường để người dùng chỉ có thể xem
        setFieldsEditable(false);
    }
    
    // Đặt trạng thái cho phép nhập/chỉnh sửa của các ô TextField
    private void setFieldsEditable(boolean editable) {
        NVnhansu_txma.setEditable(editable);
        NVnhansu_txten.setEditable(editable);
        NVnhansu_txgioitinh.setEditable(editable);
        NVnhansu_datengaysinh.setEditable(editable);
        NVnhansu_txcccd.setEditable(editable);
        NVnhansu_txemail.setEditable(editable);
        NVnhansu_txsdt.setEditable(editable);
        NVnhansu_txmaPB.setEditable(editable);
        NVnhansu_txchucvu.setEditable(editable);
    }

     // Đóng cửa sổ hiện tại khi bấm nút "Quay lại"
    @FXML
    private void NVnhansu_quaylaiAction() {
        // Lấy Stage hiện tại và đóng nó
        Stage stage = (Stage) NVnhansu_btquaylai.getScene().getWindow();
        stage.close();
    }
}

