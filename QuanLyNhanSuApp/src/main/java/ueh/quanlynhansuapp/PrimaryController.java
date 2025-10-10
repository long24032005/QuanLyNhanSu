package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void phongban_suaAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang vào chức năng sửa");
        App.setRoot("suaphongban");
    }
}
