package ueh.quanlynhansuapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/*
 Lớp App là lớp khởi động của ứng dụng
 - Thực hiện tạo cửa sổ chính (Stage) và nạp giao diện đầu tiên (FXML)
 - Cung cấp các phương thức để đổi giao diện trong chương trình
 */
public class App extends Application {
    // Scene dùng chung để tiện đổi giao diện giữa các controller
    private static Scene scene;

    // Khởi động ứng dụng, hiển thị giao diện đăng nhập đầu tiên
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setTitle("Phần mềm Quản lý Nhân sự UEH");
        stage.show();
    }

    // Cho phép controller khác cập nhật scene hiện tại
    public static void setScene(Scene newScene) {
        scene = newScene;
    }

    // Đổi giao diện (root FXML) hiện tại. Nếu Stage tồn tại, cập nhật lại kích thước và căn giữa cửa sổ.
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        Stage stage = (Stage) scene.getWindow();
        if (stage != null) {
            stage.sizeToScene();
            stage.centerOnScreen();
        }
    }

    // Hàm tải FXML chung
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //  Điểm khởi đầu chương trình
    public static void main(String[] args) {
        launch();
    }
}