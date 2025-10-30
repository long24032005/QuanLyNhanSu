package ueh.quanlynhansuapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // 🔹 Khởi động từ giao diện đăng nhập
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setTitle("Phần mềm Quản lý Nhân sự UEH");
        stage.show();
    }

    // Cho phép controller khác cập nhật scene tĩnh
    public static void setScene(Scene newScene) {
        scene = newScene;
    }

    // Cho phép đổi giao diện (root FXML)
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

    public static void main(String[] args) {
        launch();
    }
}