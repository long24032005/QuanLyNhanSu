package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class SuaPhongBan {

    @FXML
    private void suaphongban_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("phongban");
    }
}