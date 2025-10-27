package ueh.quanlynhansuapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Tải giao diện ban đầu là "Main.fxml"
        // Scene sẽ tự động lấy kích thước từ file FXML
        scene = new Scene(loadFXML("Main"));
        stage.setTitle("Phần mềm Quản lý Nhân sự - Nhóm 5");
        stage.setScene(scene);
        stage.show();
    }
    
    /*public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }*/

    public static void setRoot(String fxml) {
    try {
        scene.setRoot(loadFXML(fxml));
        System.out.println("✅ Đã chuyển sang giao diện: " + fxml);
    } catch (IOException e) {
        System.err.println("❌ Lỗi khi load FXML: " + fxml);
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("❌ Lỗi khác khi load FXML:");
        e.printStackTrace();
    }
}
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}