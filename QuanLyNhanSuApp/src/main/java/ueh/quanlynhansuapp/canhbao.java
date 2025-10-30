/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/*
    Lớp tiện ích dùng để hiển thị các loại cảnh báo và thông báo trên giao diện
    Cung cấp 4 phương thức:
    - canhbao(): hiển thị cảnh báo lỗi hoặc sai sót
    - thongbao(): hiển thị thông tin chung
    - thongbaoChoxacnhan(): hiển thị thông tin và thực hiện hành động sau khi người dùng bấm OK
    - xacNhan(): hiển thị hộp thoại xác nhận và trả về kết quả true/false
*/


public class canhbao {
    // hiển thị cảnh báo dạng WARNING
    public static void canhbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
    // hiển thị thông báo dạng INFORMATION
    public static void thongbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
    
    // Hiển thị thông báo cho phép kiểm soát thời điểm OK
    public static void thongbaoChoxacnhan(String title, String message, Runnable onOK) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
        
        // nếu có hành động được truyền vào, thực thi sau khi người dùng xác nhận
        if (onOK != null) {
            onOK.run();
        }
    }
    
    // Hiển thị hộp thoại xác nhận (OK / Cancel)
    // Trả về true nếu người dùng chọn OK, ngược lại false
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
