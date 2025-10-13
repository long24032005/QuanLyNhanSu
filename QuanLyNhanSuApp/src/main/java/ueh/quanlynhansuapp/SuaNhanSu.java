package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class SuaNhanSu {
    @FXML
    Textfield suanhansu_txma;
    @FXML
    Textfield suanhansu_txten;
    @FXML
    Choicebox suanhansu_cbgioitinh;
    @FXML
    Datepicker suanhansu_datengaysinh;
    @FXML
    Texfield suanhansu_txcccd;
    @FXML
    Textfield suanhansu_txemail;
    @FMXL
    Textfield suanhansu_txsdt;
    @FXML
    Choicebox suanhansu_cbmaPB;
    @FXML
    Choicebox suanhansu_cbchucvu;
    @FXML
    Button suanhansu_btsua;
    @FXML
    Button suanhansu_bttrolai;
    
    private void suanhansu_trolaiAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang thoát chức năng sửa");
        App.setRoot("nhansu");
    }
}
