package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController {
    
// KHAI BÁO BIẾN CHO BẢNG PHÒNG BAN  

// Khai báo cho các ô nhập liệu (TextField)
    @FXML private TextField phongban_txma;
    @FXML private TextField phongban_txten;
    @FXML private TextField phongban_txmaTP;
    @FXML private TextField phongban_txsdt;
    @FXML private TextField phongban_txemail;
    @FXML private TextField phongban_txtong;

// Khai báo cho các nút bấm (Button)
    @FXML private Button phongban_btthem;
    @FXML private Button phongban_btxoa;
    @FXML private Button phongban_btsua;
    @FXML private Button phongban_bttimkiem;

// Khai báo cho Bảng (TableView) và các Cột (TableColumn)
    @FXML private TableView<PhongBan> phongban_tbphongban;
    @FXML private TableColumn<PhongBan, String> phongban_colma;
    @FXML private TableColumn<PhongBan, String> phongban_colten;
    @FXML private TableColumn<PhongBan, String> phongban_colmaTP;
    @FXML private TableColumn<PhongBan, String> phongban_colsdt;
    @FXML private TableColumn<PhongBan, String> phongban_colemail;
    @FXML private TableColumn<PhongBan, Integer> phongban_coltong;
    
    
    @FXML
    public void initialize() {  // phần initialize() này tự động chạy khi giao diện được tải lên
                                // và nhiệm vụ của nó là chuẩn bị và cài đặt mọi thứ sẵn sàng
        
   
    // BƯỚC 1: KẾT NỐI CÁC CỘT VỚI THUỘC TÍNH DỮ LIỆU
    // Ra lệnh cho mỗi cột biết nó cần lấy dữ liệu từ thuộc tính nào của lớp PhongBan
    phongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
    phongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
    phongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
    phongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
    phongban_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
    phongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));

 
    // BƯỚC 2: TẠO DỮ LIỆU MẪU VÀ HIỂN THỊ LÊN BẢNG 
    // (Sau này bạn sẽ thay phần này bằng code lấy dữ liệu từ database)
    ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList(
    new PhongBan("P00", "Chờ phân công", null, "", "", 0),
    new PhongBan("P01", "Phòng Kỹ thuật", "NV01", "0901234567", "kythuat@cty.com", 15),
    new PhongBan("P02", "Phòng Kinh doanh", "NV03", "0907654321", "kinhdoanh@cty.com", 25),
    new PhongBan("P03", "Phòng Nhân sự", "NV05", "0908888999", "nhansu@cty.com", 5)
);
    
    // Ra lệnh cho cái bảng hiển thị danh sách vừa tạo
    phongban_tbphongban.setItems(dsPhongBan);
    

    // BƯỚC 3: THEO DÕI KHI NGƯỜI DÙNG BẤM VÀO BẢNG 
   
    // Thêm một "công cụ" vào bảng để theo dõi xem người dùng có chọn dòng nào không
    phongban_tbphongban.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
            // Khi người dùng bấm vào một dòng, `newValue` chính là phòng ban được chọn
            if (newValue != null) {
                // Lấy thông tin từ phòng ban được chọn và điền vào các ô nhập liệu
                phongban_txma.setText(newValue.getMaPhong());
                phongban_txten.setText(newValue.getTenPhong());
                phongban_txmaTP.setText(newValue.getMaTruongPhong());
                phongban_txsdt.setText(newValue.getSdt());
                phongban_txemail.setText(newValue.getEmail());
                // Chuyển số thành chữ để hiển thị. Dấu "" + ... là một mẹo đơn giản.
                phongban_txtong.setText("" + newValue.getTongSoNhanVien()); 
            }
        }
    );

} //Dấu ngoặc nhọn này là kết thúc của phần initialize()

    
  

 // Cách hoạt động của  nút Xóa ở bảng Nhân sự
 // 1. Kiểm tra xem nhân viên bạn muốn xóa có phải là trưởng phòng không
 // 2. NẾU NHÂN VIÊN BẠN MUỐN XÓA LÀ TRƯỞNG PHÒNG:
 // a. Hiển thị cảnh báo đầu tiên: "Đây là trưởng phòng... Bạn có muốn tiếp tục?"
 // b. Nếu đồng ý -> Hiển thị xác nhận cuối cùng: "Bạn có chắc chắn muốn xóa?"
 // c. Nếu đồng ý -> Xóa, cập nhật phòng ban, và thông báo phòng ban trống trưởng phòng
 // 3. NẾU NHÂN VIÊN BẠN MUỐN XÓA LÀ NHÂN VIÊN THƯỜNG:
 // a. Hiển thị xác nhận cuối cùng: "Bạn có chắc chắn muốn xóa?"
 // b. Nếu đồng ý -> Xóa và thông báo thành công

    @FXML
    private void nhansu_xoaAction() {
    // Bước 1: Lấy nhân viên đang được chọn từ bảng 'nhansu_tbnhansu'
    NhanSu selectedNhanSu = nhansu_tbnhansu.getSelectionModel().getSelectedItem();

    // Kiểm tra xem đã chọn ai chưa
    if (selectedNhanSu == null) {
        hienThongBaoLoi("Chua chon nhan vien", "Vui long chon mot nhan vien đe xoa.");
        return;
    }

    // Bước 2: Kiểm tra xem nhân viên này có phải là trưởng phòng không
    PhongBan phongBanCuaNhanVien = null; // Biến để lưu phòng ban mà nhân viên này làm trưởng phòng
    for (PhongBan pb : dsPhongBan) {
        if (pb.getTruongPhong() != null && pb.getTruongPhong().equals(selectedNhanSu.getMaNV())) {
            phongBanCuaNhanVien = pb; // Tìm thấy rồi, lưu lại và thoát khỏi vòng lặp
            break;
        }
    }

    // Bước 3: Dựa vào kết quả kiểm tra để xử lý theo 2 trường hợp khác nhau
    if (phongBanCuaNhanVien != null) {
        // TRƯỜNG HỢP 1: XÓA TRƯỞNG PHÒNG (Yêu cầu xác nhận nhiều bước)
        
        // a. Hiển thị CẢNH BÁO ĐẦU TIÊN
        Alert canhBaoDauTien = new Alert(Alert.AlertType.WARNING);
        canhBaoDauTien.setTitle("Canh bao quan trong");
        canhBaoDauTien.setHeaderText("Nhan vien '" + selectedNhanSu.getHoTen() + "' đang là truong phong cua '" 
                                    + phongBanCuaNhanVien.getTenPhong() + "'.");
        canhBaoDauTien.setContentText("Hanh đong nay se khien phong ban khong co nguoi quan ly. Ban co muon tiep tuc?");

        // Chờ người dùng đồng ý cảnh báo đầu tiên
        if (canhBaoDauTien.showAndWait().get() == ButtonType.OK) {
            
            // b. Hiển thị XÁC NHẬN CUỐI CÙNG
            Alert xacNhanCuoiCung = new Alert(Alert.AlertType.CONFIRMATION);
            xacNhanCuoiCung.setTitle("Xac nhan lan cuoi");
            xacNhanCuoiCung.setHeaderText("Ban co chac chan muon xoa vinh vien nhan vien nay?");

            // Chờ người dùng đồng ý xác nhận cuối cùng
            if (xacNhanCuoiCung.showAndWait().get() == ButtonType.OK) {
                // c. Thực hiện hành động
                String tenPhongBan = phongBanCuaNhanVien.getTenPhong();
                
                phongBanCuaNhanVien.setTruongPhong(null); // Cập nhật phòng ban
                dsNhanSu.remove(selectedNhanSu); // Xóa nhân viên
                
                // Hiển thị thông báo KẾT QUẢ
                hienThongBaoThanhCong("Hoan tat", "Da xoa truong phong. Phong ban '" 
                                     + tenPhongBan + "' hien khong co nguoi quan ly.");
                
                
                phongban_tbphongban.refresh();
            }
        }

    } else {
        // TRƯỜNG HỢP 2: XÓA NHÂN VIÊN THƯỜNG (Chỉ cần 1 lần xác nhận)
        Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
        xacNhan.setTitle("Xac nhan xoa");
        xacNhan.setHeaderText("Ban co chac chan muon xoa nhan vien: " + selectedNhanSu.getHoTen() + "?");

        if (xacNhan.showAndWait().get() == ButtonType.OK) {
            dsNhanSu.remove(selectedNhanSu);
            hienThongBaoThanhCong("Thanh cong", "Da xoa nhan vien.");
        }
    }
}

    


 // Cách hoạt động của  nút Xóa ở bảng Phòng ban
 // Tự động chuyển tất cả nhân viên trong phòng ban bị xóa sang phòng "Chờ phân công" (mã P00) trước khi xóa
 
 
    @FXML
    private void phongban_xoaAction() {
    // 1. Lấy phòng ban mà người dùng đang chọn trong bảng
    PhongBan selectedPhongBan = phongban_tbphongban.getSelectionModel().getSelectedItem();

    // 2. Kiểm tra xem người dùng đã chọn phòng ban nào chưa
    if (selectedPhongBan == null) {
        hienThongBaoLoi("Chua chon phong ban", "Vui long chon mot phong ban de xaa.");
        return; // Dừng hàm nếu chưa chọn
    }

    // 3. Hiển thị hộp thoại xác nhận, báo trước về việc tự động chuyển nhân viên
    Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
    xacNhan.setTitle("Xac nhan xoa và di chuyen");
    xacNhan.setHeaderText("Ban co chac chan muon xoa phong ban: " + selectedPhongBan.getTenPhong() + "?");
    xacNhan.setContentText("Luu y: Tat ca nhan vien trong phong nay se duoc tu dong chuyen sang phong 'Cho phan cong'.");

    // 4. Chờ người dùng nhấn OK, nếu đồng ý mới thực hiện
    if (xacNhan.showAndWait().get() == ButtonType.OK) {
        
        // a. Lưu lại mã của phòng ban sắp bị xóa để dùng cho việc tìm kiếm
        String maPhongCanXoa = selectedPhongBan.getMaPhong();

        // b. Quét qua danh sách nhân viên để tìm và cập nhật
        for (NhanSu ns : dsNhanSu) {
            // Nếu tìm thấy nhân viên thuộc phòng ban sắp bị xóa...
            if (ns.getMaPhong() != null && ns.getMaPhong().equals(maPhongCanXoa)) {
                // ...thì chuyển họ sang phòng ban mặc định "P00"
                ns.setMaPhong("P00");
            }
        }

        // c. Sau khi đã chuyển hết nhân viên, tiến hành xóa phòng ban
        dsPhongBan.remove(selectedPhongBan);

        // d. Yêu cầu bảng nhân sự tự làm mới để hiển thị thông tin phòng ban mới
        nhansu_tbnhansu.refresh();

        // e. Hiển thị thông báo hoàn tất
        hienThongBaoThanhCong("Thanh cong", "Da xoa phong ban và di chuyen nhan vien.");
    }
}
    
    
    
    @FXML
    private void phongban_suaAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang vào chức năng sửa");
        App.setRoot("suaphongban");
    }
}
