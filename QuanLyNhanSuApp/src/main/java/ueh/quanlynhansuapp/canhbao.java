/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author lagia
 */
public class canhbao {
    public static void canhbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
    
    public static void thongbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
    
    public static boolean xacNhan(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Hiển thị hộp thoại và chờ người dùng phản hồi
        Optional<ButtonType> result = alert.showAndWait();

        // Kiểm tra xem người dùng có nhấn nút OK không
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
