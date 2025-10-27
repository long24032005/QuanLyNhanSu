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
import java.io.File;
import java.io.FileOutputStream;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    @FXML private Button phongban_btxuat;
    @FXML private Button nhansu_btquaylai;

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
        phongban_txtong.setEditable(false);
    }
    private boolean isNumeric(String str) {
        if (str == null) 
            return false;
    // Sử dụng regex để kiểm tra chuỗi có chứa TOÀN BỘ là số hay không
        return str.matches("\\d+");
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

        if (maPhong.length() != 4) {
            canhbao.canhbao("Sai định dạng", "Mã phòng ban phải có đúng 4 ký tự.");
            return;
        }
    
        // Kiểm tra sdtPhong (nếu người dùng có nhập)
        if (!sdtPhong.isEmpty()) {
            if (!isNumeric(sdtPhong) || sdtPhong.length() != 10) {
                canhbao.canhbao("Sai định dạng", "Số điện thoại phòng ban (nếu nhập) phải là 10 ký tự số.");
            return;
            }
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


               

               


                "Tất cả nhân viên trong phòng này sẽ được chuyển sang phòng 'PB00 - Phòng ban Chờ phân công'."

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
    
    @FXML
    private void phongban_xuatAction() {
        PhongBan selected = phongban_tbphongban.getSelectionModel().getSelectedItem();
        if (selected == null) {
            canhbao.canhbao("Chưa chọn phòng ban", "Vui lòng chọn 1 hàng trước khi xuất thông tin.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Bạn có muốn xuất thông tin tất cả nhân viên của phòng \"" + selected.getTenPhong() + "\" ra file Excel không?", 
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận xuất file");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.NO) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn nơi lưu file Excel");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel File (*.xlsx)", "*.xlsx"));
        fileChooser.setInitialFileName("NhanVien_" + selected.getTenPhong() + ".xlsx");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) return;

        ProgressIndicator progressIndicator = new ProgressIndicator();
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Đang xuất file...");
        progressAlert.setHeaderText("Vui lòng chờ trong giây lát...");
        progressAlert.getDialogPane().setContent(progressIndicator);
        progressAlert.show();

        Task<Void> exportTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                exportNhanSuTheoPhong(file, selected.getMaPhong());
                return null;
            }
        };

        exportTask.setOnSucceeded(e -> {
            progressAlert.close();
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Xuất thành công!");
            success.setHeaderText("Đã xuất thông tin thành công!");
            success.setContentText("File đã lưu tại:\n" + file.getAbsolutePath());

            ButtonType openBtn = new ButtonType("Mở file");
            ButtonType backBtn = new ButtonType("Quay lại");
            success.getButtonTypes().setAll(openBtn, backBtn);
            success.showAndWait();

            if (success.getResult() == openBtn) {
                try {
                    java.awt.Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    canhbao.canhbao("Lỗi", "Không thể mở file vừa tạo.");
                }
            }
        });

        exportTask.setOnFailed(e -> {
            progressAlert.close();
            canhbao.canhbao("Lỗi", "Không thể xuất file Excel.\nChi tiết: " + exportTask.getException());
        });

        new Thread(exportTask).start();
    }

    // === HÀM HỖ TRỢ ===
    private void exportNhanSuTheoPhong(File file, String maPhong) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("NhanVien_" + maPhong);
            Row header = sheet.createRow(0);
            String[] headers = {
                "Mã NV", "Họ tên", "Giới tính", "Ngày sinh",
                "CCCD", "Email", "SĐT", "Phòng ban", "Chức vụ"
            };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            ObservableList<NhanSu> list = dataService.getDsNhanSu();
            int rowNum = 1;
            for (NhanSu ns : list) {
                if (ns.getMaPhongBan().equals(maPhong)) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(ns.getMaNV());
                    row.createCell(1).setCellValue(ns.getHoTen());
                    row.createCell(2).setCellValue(ns.getGioiTinh());
                    row.createCell(3).setCellValue(ns.getNgaySinh() != null ? ns.getNgaySinh().toString() : "");
                    row.createCell(4).setCellValue(ns.getCccd());
                    row.createCell(5).setCellValue(ns.getEmail());
                    row.createCell(6).setCellValue(ns.getSdt());
                    row.createCell(7).setCellValue(ns.getMaPhongBan());
                    row.createCell(8).setCellValue(ns.getChucVu());
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }



    
    
    @FXML
    private void phongban_quaylaiAction() throws IOException {
        App.setRoot("main");
    }
    
    
}

