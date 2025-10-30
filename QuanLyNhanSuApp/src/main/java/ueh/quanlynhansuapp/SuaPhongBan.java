package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
/*
    Controller cho giao diện "Sửa phòng ban", phân quyền Admin (suaphongban.fxml)
    Chức năng:
    - Hiển thị thông tin phòng ban được chọn
    - Cho phép chỉnh sửa tên, trưởng phòng, số điện thoại và email
    - Kiểm tra dữ liệu hợp lệ trước khi lưu
*/
public class SuaPhongBan { 
    @FXML
    private TextField suaphongban_txma;
    @FXML
    private TextField suaphongban_txten;
    @FXML
    private ComboBox<String> suaphongban_cbmaTP;
    @FXML
    private TextField suaphongban_txsdt;
    @FXML
    private TextField suaphongban_txemail;
    @FXML
    private TextField suaphongban_txtong;
    @FXML
    private Button suaphongban_btsua;
    @FXML
    private Button suaphongban_bttrolai;
    
    private PhongBan pBan;  // đối tượng phòng ban đang được chỉnh sửa
    
    private final DataService dataService = DataService.getInstance(); ; // truy cập dữ liệu chung
     
    // nhận dữ liệu từ giao diện chính và hiển thị lên form
    public void setData(PhongBan pBan) {
        this.pBan = pBan;
        suaphongban_txma.setText(pBan.getMaPhong());
        suaphongban_txten.setText(pBan.getTenPhong());
        suaphongban_txsdt.setText(pBan.getSdtPhong());
        suaphongban_txemail.setText(pBan.getEmailPhong());
        suaphongban_txtong.setText(String.valueOf(pBan.getTongSoNhanVien()));
        
        // Lấy danh sách nhân viên chỉ thuộc phòng ban này
        ObservableList<String> nhanVienTrongPhong = DataService.getInstance().getDsNhanSu()
                .stream() // Bắt đầu xử lý luồng dữ liệu
                .filter(ns -> ns.getMaPhongBan().equals(pBan.getMaPhong())) // Lọc ra những ai có mã phòng ban trùng khớp
                .map(NhanSu::getMaNV) // Chỉ lấy mã nhân viên của họ
                .collect(Collectors.toCollection(FXCollections::observableArrayList)); // Thu thập kết quả vào một danh sách mới

        // Thêm một lựa chọn "Bỏ trống" để có thể không chọn ai làm trưởng phòng
        nhanVienTrongPhong.add(0, "(Bỏ trống)");
        
        // Gán danh sách này cho ComboBox
        suaphongban_cbmaTP.setItems(nhanVienTrongPhong);
        
        // Chọn sẵn trưởng phòng hiện tại (nếu có), nếu không chọn dòng đầu tiên (bỏ trống)
        if (pBan.getMaTruongPhong() != null && !pBan.getMaTruongPhong().isEmpty()) {
            suaphongban_cbmaTP.setValue(pBan.getMaTruongPhong());
        } else {
            suaphongban_cbmaTP.getSelectionModel().selectFirst(); // Chọn "Bỏ trống"
        }

        // mã phòng và tổng số nhân viên là thông tin chỉ xem
        suaphongban_txma.setDisable(true);
        suaphongban_txtong.setDisable(true); 
    }
    
    // kiểm tra chuỗi có phải toàn số hay không
    private boolean isNumeric(String str) {
        if (str == null) return false;
        return str.matches("\\d+");
    }
    
    //  Nút "Sửa"
    @FXML
    private void suaphongban_suaAction() throws IOException {
        String ten = suaphongban_txten.getText().trim();
        String maTP = suaphongban_cbmaTP.getValue();
        String sdt = suaphongban_txsdt.getText().trim();
        String email = suaphongban_txemail.getText().trim();
        // Xử lý trường hợp người dùng chọn "Bỏ trống"
        if (maTP != null && maTP.contains("Bỏ trống")) {
            maTP = null; // Lưu giá trị null hoặc rỗng vào file Excel
        }

        // kiểm tra tên phòng ban không được để trống
        if (ten.isEmpty()) {
        canhbao.canhbao("Lỗi", "Tên phòng ban không được để trống!");
        return;
    }

        // Kiểm tra SĐT (nếu người dùng có nhập)
        if (!sdt.isEmpty()) {
            if (!isNumeric(sdt) || sdt.length() != 10) {
                canhbao.canhbao("Sai định dạng", "Số điện thoại (nếu nhập) phải là 10 ký tự số.");
                return;
            }
        }
        
        // Nếu có nhập mã trưởng phòng thì kiểm tra xem mã nhân sự đó có tồn tại không
        if (maTP != null && !maTP.isEmpty()) {
            boolean tonTaiNhanSu = false;
            for (NhanSu ns : dataService.getDsNhanSu()) {
                if (ns.getMaNV().equalsIgnoreCase(maTP)) {
                    tonTaiNhanSu = true;
                    break;
                }
            }

            if (!tonTaiNhanSu) {
                canhbao.canhbao("Không tồn tại", 
                    "Mã nhân viên \"" + maTP + "\" không tồn tại trong danh sách nhân sự.");
                return;
            }
        }
        
        // Kiểm tra trùng tên, email, mã trưởng phòng (ngoại trừ chính phòng hiện tại)
        for (PhongBan pb : dataService.getDsPhongBan()) {
            if (!pb.getMaPhong().equals(pBan.getMaPhong())) {

                if (pb.getTenPhong() != null && pb.getTenPhong().trim().equalsIgnoreCase(ten)) {
                    canhbao.canhbao("Trùng tên", "Tên phòng ban \"" + ten + "\" đã tồn tại.");
                    return;
                }

                if (!email.isEmpty() && pb.getEmailPhong() != null &&
                    pb.getEmailPhong().trim().equalsIgnoreCase(email)) {
                    canhbao.canhbao("Trùng email", "Email \"" + email + "\" đã được sử dụng bởi phòng khác.");
                    return;
                }

                if (maTP != null && !maTP.isEmpty() &&
                    pb.getMaTruongPhong() != null &&
                    pb.getMaTruongPhong().equalsIgnoreCase(maTP)) {
                    canhbao.canhbao("Trùng trưởng phòng", "Mã trưởng phòng \"" + maTP + "\" đã được dùng ở phòng \"" + pb.getTenPhong() + "\".");
                    return;
                }
            }
        }

        
         // Popup xác nhận lưu thay đổi
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận sửa thông tin");
        confirm.setHeaderText("Bạn có chắc chắn muốn lưu thay đổi?");

        ButtonType yesBtn = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yesBtn, cancelBtn);   
        confirm.showAndWait();

        // Nếu người dùng bấm Hủy => dừng
        if (confirm.getResult() == cancelBtn) {
            canhbao.thongbao("Đã hủy", "Không có thay đổi nào được lưu.");
            return;
        }

        // Nếu người dùng bấm "Xác nhận" 
        pBan.setTenPhong(ten);
        pBan.setMaTruongPhong(maTP); // Cập nhật mã trưởng phòng mới
        pBan.setSdtPhong(sdt);
        pBan.setEmailPhong(email);
        
        // lưu vào hệ thống
        DataService.getInstance().updatePhongBan(pBan);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Đã cập nhật thông tin phòng ban! Nhấn OK để quay lại.");
        alert.showAndWait();

        // Sau khi người dùng nhấn OK => quay lại màn hình phòng ban
        FXMLLoader loader = new FXMLLoader(getClass().getResource("phongban.fxml"));
        Parent root = loader.load();
        suaphongban_btsua.getScene().setRoot(root);
    }
    
    // Nút "Trở lại"
    @FXML
    private void suaphongban_trolaiAction() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận trở lại");
        alert.setHeaderText("Bạn có chắc muốn quay về màn hình Phòng Ban?");
        alert.setContentText("Mọi thay đổi chưa lưu sẽ bị mất.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("phongban.fxml"));
            Parent root = loader.load();
            suaphongban_bttrolai.getScene().setRoot(root);
        }
    }
}