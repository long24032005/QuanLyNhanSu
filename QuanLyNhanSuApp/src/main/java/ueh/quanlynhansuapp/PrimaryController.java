package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController {
    
// --- KHAI BÁO BIẾN CHO BẢNG PHÒNG BAN ---
    @FXML private TextField phongban_txma;
    @FXML private TextField phongban_txten;
    @FXML private TextField phongban_txmaTP;
    @FXML private TextField phongban_txsdt;
    @FXML private TextField phongban_txemail;
    @FXML private TextField phongban_txtong;

    @FXML private Button phongban_btthem;
    @FXML private Button phongban_btxoa;
    @FXML private Button phongban_btsua;
    @FXML private Button phongban_bttimkiem;

    @FXML private TableView<PhongBan> phongban_tbphongban;
    @FXML private TableColumn<PhongBan, String> phongban_colma;
    @FXML private TableColumn<PhongBan, String> phongban_colten;
    @FXML private TableColumn<PhongBan, String> phongban_colmaTP;
    @FXML private TableColumn<PhongBan, String> phongban_colsdt;
    @FXML private TableColumn<PhongBan, String> phongban_colemail;
    @FXML private TableColumn<PhongBan, Integer> phongban_coltong;

    // Lấy instance của DataService để truy cập dữ liệu chung
    private final DataService dataService = DataService.getInstance();

    @FXML
    public void initialize() {
        // --- CÀI ĐẶT CÁC CỘT CHO BẢNG PHÒNG BAN ---
        phongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        phongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
        phongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
        phongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdtPhong"));
        phongban_colemail.setCellValueFactory(new PropertyValueFactory<>("emailPhong"));
        phongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));

        // --- GÁN NGUỒN DỮ LIỆU TỪ DATASERVICE ---
        phongban_tbphongban.setItems(dataService.getDsPhongBan());

        // --- SỰ KIỆN KHI CHỌN MỘT DÒNG TRONG BẢNG ---
        phongban_tbphongban.getSelectionModel().selectedItemProperty().addListener((obs, oldV, pb) -> {
            if (pb != null) {
                phongban_txma.setText(pb.getMaPhong());
                phongban_txten.setText(pb.getTenPhong());
                phongban_txmaTP.setText(pb.getMaTruongPhong());
                phongban_txsdt.setText(pb.getSdtPhong());
                phongban_txemail.setText(pb.getEmailPhong());
                phongban_txtong.setText(String.valueOf(pb.getTongSoNhanVien()));
            }
        });
    }

    @FXML
    private void phongban_themAction() {
        String maPhong = phongban_txma.getText().trim();
        String tenPhong = phongban_txten.getText().trim();
        String maTP = phongban_txmaTP.getText().trim();
        String sdtPhong = phongban_txsdt.getText().trim();
        String emailPhong = phongban_txemail.getText().trim();

        if (maPhong.isEmpty() || tenPhong.isEmpty()) {
            canhbao.canhbao("Thiếu thông tin", "Mã phòng và Tên phòng là bắt buộc.");
            return;
        }

        if (dataService.timPhongBanTheoMa(maPhong) != null) {
            canhbao.canhbao("Trùng mã", "Mã phòng \"" + maPhong + "\" đã tồn tại.");
            return;
        }
        if (!maTP.isEmpty() && dataService.timNhanSuTheoMa(maTP) == null) {
            canhbao.canhbao("Trưởng phòng không tồn tại", "Mã nhân sự \"" + maTP + "\" không có trong hệ thống.");
            return;
        }

        PhongBan pb = new PhongBan(maPhong, tenPhong, maTP.isEmpty() ? null : maTP, sdtPhong, emailPhong, 0);
        
        // Thêm thông qua DataService để đồng bộ với Database
        if (dataService.addPhongBan(pb)) {
            canhbao.thongbao("Thành công", "Đã thêm phòng ban \"" + tenPhong + "\".");
            phongban_txma.clear();
            phongban_txten.clear();
            phongban_txmaTP.clear();
            phongban_txsdt.clear();
            phongban_txemail.clear();
            phongban_txtong.clear();
        }
    }
    
    @FXML 
    private void phongban_xoaAction() { 
        PhongBan selectedPhongBan = phongban_tbphongban.getSelectionModel().getSelectedItem();

        if (selectedPhongBan == null) {
            canhbao.canhbao("Chưa chọn phòng ban", "Vui lòng chọn một phòng ban để xóa.");
            return;
        }

        boolean dongY = canhbao.xacNhan(
                "Xác nhận xóa và di chuyển",
                "Bạn có chắc chắn muốn xóa phòng ban: " + selectedPhongBan.getTenPhong() + "?",
                "Tất cả nhân viên trong phòng này sẽ được chuyển sang phòng 'Chờ phân công'."
        );

        if (dongY) {
            // Xóa thông qua DataService để xử lý logic nghiệp vụ và DB
            dataService.deletePhongBan(selectedPhongBan);
            canhbao.thongbao("Thành công", "Đã xóa phòng ban và di chuyển nhân viên.");
        }
    }
    
    @FXML 
    private void phongban_suaAction() throws IOException  { 
        PhongBan pbselected = phongban_tbphongban.getSelectionModel().getSelectedItem();
        if(pbselected == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"Chọn 1 hàng để sửa", ButtonType.YES);
            a.setTitle("Thông Tin");
            a.showAndWait();
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("suaphongban.fxml"));
        Parent root = loader.load();
    
        // Lấy controller của màn hình "suaphongban"
        SuaPhongBan controller = loader.getController();
        // Gọi hàm setData để truyền phòng ban đã chọn qua
        controller.setData(pbselected);
    
        // Lấy Scene hiện tại và set root mới
        phongban_btsua.getScene().setRoot(root);
    }
    
    @FXML 
    private void phongban_timkiemAction() throws IOException { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timkiemphongban.fxml"));
        Parent root = loader.load();

        // Lấy controller của màn hình "timkiemphongban"
        TimKiemPhongBan controller = loader.getController();
        // Gọi hàm setData để truyền toàn bộ danh sách phòng ban qua
        controller.setData(dataService.getDsPhongBan());
    
        // Lấy Scene hiện tại và set root mới
        phongban_bttimkiem.getScene().setRoot(root);
    }
}

