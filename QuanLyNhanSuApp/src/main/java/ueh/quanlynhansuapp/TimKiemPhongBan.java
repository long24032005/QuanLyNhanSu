/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


/*
    Controller cho giao diện "Tìm kiếm phòng ban", phân quyền Admin (timkiemphongban.fxml)
    Chức năng:
    - Cho phép nhập mã phòng ban để tìm kiếm
    - Hiển thị kết quả trong bảng
    - Chỉ hiển thị thông tin, không cho chỉnh sửa
*/
public class TimKiemPhongBan {
    // Khai báo cho các ô nhập liệu (TextField)
    @FXML private TextField timkiemphongban_txma;
    @FXML private TextField timkiemphongban_txten;
    @FXML private TextField timkiemphongban_txmaTP;
    @FXML private TextField timkiemphongban_txsdt;
    @FXML private TextField timkiemphongban_txemail;
    @FXML private TextField timkiemphongban_txtong;

    // Khai báo cho các nút bấm (Button)
    @FXML private Button timkiemphongban_bttimkiem;
    @FXML private Button timkiemphongban_bttrolai;

    // Khai báo cho Bảng (TableView) và các Cột (TableColumn)
    @FXML private TableView<PhongBan> timkiemphongban_tbphongban;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colma;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colten;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colmaTP;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colsdt;
    @FXML private TableColumn<PhongBan, String> timkiemphongban_colemail;
    @FXML private TableColumn<PhongBan, Integer> timkiemphongban_coltong;

    // Danh sách gốc, chứa tất cả phòng ban được truyền từ màn hình chính
    private ObservableList<PhongBan> masterData = FXCollections.observableArrayList();
    private ObservableList<PhongBan> ketQuaTimKiem = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Chỉ cho mỗi cột biết nó sẽ lấy dữ liệu từ thuộc tính nào của lớp PhongBan
        timkiemphongban_colma.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        timkiemphongban_colten.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
        timkiemphongban_colmaTP.setCellValueFactory(new PropertyValueFactory<>("maTruongPhong"));
        timkiemphongban_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdtPhong"));
        timkiemphongban_colemail.setCellValueFactory(new PropertyValueFactory<>("emailPhong"));
        timkiemphongban_coltong.setCellValueFactory(new PropertyValueFactory<>("tongSoNhanVien"));
             
        // Gán bảng hiển thị danh sách kết quả
        timkiemphongban_tbphongban.setItems(ketQuaTimKiem);

        // Khóa các ô thông tin (chỉ để xem)
        setInfoFieldsEditable(false);

    }
    
    // khóa hoặc mở khóa các ô nhập
    private void setInfoFieldsEditable(boolean editable) {
        timkiemphongban_txten.setEditable(editable);
        timkiemphongban_txmaTP.setEditable(editable);
        timkiemphongban_txsdt.setEditable(editable);
        timkiemphongban_txemail.setEditable(editable);
        timkiemphongban_txtong.setEditable(editable);
    }
    
    // Nhận dữ liệu gốc từ controller chính
    public void setData(ObservableList<PhongBan> allPhongBan) {
        this.masterData.setAll(allPhongBan);
    }

    @FXML
    private void timkiemphongban_timkiemAction() {
        // Lấy các giá trị tìm kiếm từ các ô TextField
        String maPB_input = timkiemphongban_txma.getText().trim();
        // kiểm tra người dùng đã nhập mã phòng hay chưa 
        if (maPB_input.isEmpty()) {
            canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập Mã phòng ban để tìm kiếm!");
            return;
        }
        
        // Tìm phòng ban theo mã
        PhongBan ketQua = null;
        for (PhongBan pb : masterData) {
            if (pb.getMaPhong() != null && pb.getMaPhong().equalsIgnoreCase(maPB_input)) {
                ketQua = pb;
                break;
            }
        }
        // nếu không tìm thấy phòng ban
        if (ketQua == null) {
            canhbao.thongbao("Không tìm thấy", "Không tồn tại phòng ban có mã \"" + maPB_input + "\"");
            return;
        }

        // Kiểm tra trùng trong bảng kết quả
        boolean tonTaiTrongBang = ketQuaTimKiem.stream()
            .anyMatch(pb -> pb.getMaPhong().equalsIgnoreCase(maPB_input));

        if (!tonTaiTrongBang) {
            ketQuaTimKiem.add(ketQua);
        } else {
            canhbao.thongbao("Thông báo", "Phòng ban này đã có trong bảng kết quả.");
        }

        // Hiển thị thông tin chi tiết
        showPhongBanInfo(ketQua);
        timkiemphongban_tbphongban.getSelectionModel().select(ketQua);
    }

    // Hàm hiển thị thông tin của phòng ban lên các ô chi tiết
    private void showPhongBanInfo(PhongBan pb) {
        timkiemphongban_txten.setText(pb.getTenPhong());
        timkiemphongban_txmaTP.setText(pb.getMaTruongPhong());
        timkiemphongban_txsdt.setText(pb.getSdtPhong());
        timkiemphongban_txemail.setText(pb.getEmailPhong());
        timkiemphongban_txtong.setText(String.valueOf(pb.getTongSoNhanVien()));
    }

    // Hàm xóa thông tin trong form
    private void clearInfoFields() {
        timkiemphongban_txten.clear();
        timkiemphongban_txmaTP.clear();
        timkiemphongban_txsdt.clear();
        timkiemphongban_txemail.clear();
        timkiemphongban_txtong.clear();
    }

    // Nút "Trở lại"
    @FXML
    private void timkiemphongban_trolaiAction() throws IOException {
        boolean dongY = canhbao.xacNhan(
            "Trở lại",
            "Bạn có chắc chắn muốn quay về màn hình Phòng Ban?",
            "Mọi kết quả tìm kiếm hiện tại sẽ bị mất."
        );
        if (!dongY) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("phongban.fxml"));
        Parent root = loader.load();
        timkiemphongban_bttrolai.getScene().setRoot(root);
    }
}
