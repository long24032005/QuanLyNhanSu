package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class NhanSUController {

    @FXML
    private void nhansu_suaAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang vào chức năng sửa");
        App.setRoot("suanhansu");
    }
}
