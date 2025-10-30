package ueh.quanlynhansuapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
Controller quản lý luongthuong.fxml.
Chịu trách nhiệm:
 - Hiển thị dữ liệu bảng lương
 - Cho phép thêm / sửa / xóa bản ghi
 - Tự tính tổng lương
 - Xuất toàn bộ dữ liệu ra file Excel
 - Kiểm tra ngoại lệ
 */

public class LuongThuongController {
    // Các định dạng ngày / tháng / năm được dùng để hiển thị và parse dữ liệu
    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Các ô nhập liệu trong giao diện
    @FXML private TextField luongthuong_txmaluong;
    @FXML private TextField luongthuong_txmaNV;
    @FXML private DatePicker luongthuong_datethangnam;
    @FXML private TextField luongthuong_txluongcoban;
    @FXML private TextField luongthuong_txphucap;
    @FXML private TextField luongthuong_txthuong;
    @FXML private TextField luongthuong_txkhautru;
    @FXML private TextField luongthuong_txtongluong;
    @FXML private DatePicker luongthuong_datengaychitra;
    @FXML private Button luongthuong_btthem;
    @FXML private Button luongthuong_btxoa;
    @FXML private Button luongthuong_btsua;
    @FXML private Button luongthuong_bttimkiem;
    @FXML private Button luongthuong_btxuat;
    @FXML private Button luongthuong_btquaylai;
    // Bảng hiển thị dữ liệu lương thưởng
    @FXML private TableView<LuongThuong> luongthuong_tbluongthuong;
    @FXML private TableColumn<LuongThuong, String> luongthuong_colmaluong;
    @FXML private TableColumn<LuongThuong, String> luongthuong_colmaNV;
    @FXML private TableColumn<LuongThuong, String> luongthuong_colthangnam;
    @FXML private TableColumn<LuongThuong, Double> luongthuong_colluongcoban;
    @FXML private TableColumn<LuongThuong, Double> luongthuong_colphucap;
    @FXML private TableColumn<LuongThuong, Double> luongthuong_colthuong;
    @FXML private TableColumn<LuongThuong, Double> luongthuong_colkhautru;
    @FXML private TableColumn<LuongThuong, Double> luongthuong_coltongluong;
    @FXML private TableColumn<LuongThuong, String> luongthuong_colngaychitra;

     // Danh sách dữ liệu hiển thị trên bảng
    private final ObservableList<LuongThuong> dsLuong = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Gắn dữ liệu từ model (LuongThuong) vào từng cột của TableView
            luongthuong_colmaluong.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaLuong()));
            luongthuong_colmaNV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaNhanVien()));
            luongthuong_colthangnam.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getThangNam()));
            luongthuong_colngaychitra.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNgayChiTra()));

            luongthuong_colluongcoban.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
            luongthuong_colphucap.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPhuCap()));
            luongthuong_colthuong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getThuong()));
            luongthuong_colkhautru.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getKhauTru()));
            luongthuong_coltongluong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTongLuong()));
            
            // Căn trái các cột số
            alignLeft(luongthuong_colluongcoban);
            alignLeft(luongthuong_colphucap);
            alignLeft(luongthuong_colthuong);
            alignLeft(luongthuong_colkhautru);
            alignLeft(luongthuong_coltongluong);

            // Tải dữ liệu ban đầu từ DataService và hiển thị
            DataService.getInstance().reloadAllData();
            dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
            luongthuong_tbluongthuong.setItems(dsLuong);

            // Khi click chọn một dòng trên bảng => tự điền dữ liệu lên form
            luongthuong_tbluongthuong.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, lt) -> {
                if (lt == null) return;
                try {
                    fillForm(lt);
                } catch (Exception ex) {
                    canhbao.canhbao("Lỗi hiển thị", "Không thể hiển thị chi tiết dòng: " + ex.getMessage());
                }
            });

            // Tự động tính tổng lương khi người dùng nhập thay đổi
            ChangeListener<String> recalc = (o, a, b) -> capNhatTongLuong();
            luongthuong_txluongcoban.textProperty().addListener(recalc);
            luongthuong_txphucap.textProperty().addListener(recalc);
            luongthuong_txthuong.textProperty().addListener(recalc);
            luongthuong_txkhautru.textProperty().addListener(recalc);

            luongthuong_txtongluong.setEditable(false);
            luongthuong_txtongluong.setStyle("-fx-alignment: CENTER-LEFT");

            // Không cho nhập ký tự khác ngoài số nguyên dương
            addIntegerOnlyValidation(luongthuong_txluongcoban, "Lương cơ bản");
            addIntegerOnlyValidation(luongthuong_txphucap, "Phụ cấp");
            addIntegerOnlyValidation(luongthuong_txthuong, "Thưởng");
            addIntegerOnlyValidation(luongthuong_txkhautru, "Khấu trừ");

        } catch (Exception e) {
            canhbao.canhbao("Lỗi khởi tạo", "Không thể khởi tạo bảng dữ liệu:\n" + e.getMessage());
        }
        
        // Tùy chỉnh DatePicker để chỉ chọn tháng/năm thay vì ngày cụ thể
        luongthuong_datethangnam.setPromptText("MM/yyyy");
        // Chuyển đổi hiển thị và parse
        luongthuong_datethangnam.setConverter(new javafx.util.StringConverter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        // Dùng ngày 1 cố định để parse
                        return LocalDate.parse("01/" + string, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });
    }

    
    // Hàm hỗ trợ hiển thị

    // Căn trái cho các cột só
    private void alignLeft(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : doubleToPlain(item));
                setStyle("-fx-alignment: CENTER-LEFT;");
            }
        });
    }

    // Định dạng số: nếu là số nguyên => bỏ phần .0, nếu có phần lẻ => hiển thị 2 số sau dấu phẩy
    private String doubleToPlain(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9) return String.valueOf((long)Math.rint(v));
        return String.format("%.2f", v);
    }

    // Chuyển chuỗi nhập vào thành số double
    private double parseMoney(String s) throws NumberFormatException {
        if (s == null || s.isBlank()) return 0;
        return Double.parseDouble(s.trim());
    }

    // Tính lại tổng lương mỗi khi người dùng nhập thay đổi
    private void capNhatTongLuong() {
        try {
            double luongCoBan = parseMoney(luongthuong_txluongcoban.getText());
            double phuCap = parseMoney(luongthuong_txphucap.getText());
            double thuong = parseMoney(luongthuong_txthuong.getText());
            double khauTru = parseMoney(luongthuong_txkhautru.getText());
            double tong = luongCoBan + phuCap + thuong - khauTru;
            if (tong < 0) tong = 0;
            luongthuong_txtongluong.setText(doubleToPlain(tong));
        } catch (Exception ignored) {
            luongthuong_txtongluong.clear();
        }
    }
    
    // Điền dữ liệu của bản ghi được chọn vào form nhập liệu
    private void fillForm(LuongThuong lt) {
        luongthuong_txmaluong.setText(lt.getMaLuong());
        luongthuong_txmaNV.setText(lt.getMaNhanVien());
        luongthuong_txluongcoban.setText(doubleToPlain(lt.getLuongCoBan()));
        luongthuong_txphucap.setText(doubleToPlain(lt.getPhuCap()));
        luongthuong_txthuong.setText(doubleToPlain(lt.getThuong()));
        luongthuong_txkhautru.setText(doubleToPlain(lt.getKhauTru()));
        luongthuong_txtongluong.setText(doubleToPlain(lt.getTongLuong()));

         // Parse lại ngày tháng lương và ngày chi trả
        try {
            luongthuong_datethangnam.setValue(LocalDate.parse("01/" + lt.getThangNam(), DMY));
        } catch (DateTimeParseException e) {
            luongthuong_datethangnam.setValue(null);
        }

        try {
            String ngay = lt.getNgayChiTra();
            LocalDate d = ngay.contains("/") ? LocalDate.parse(ngay, DMY) : LocalDate.parse(ngay, YMD);
            luongthuong_datengaychitra.setValue(d);
        } catch (DateTimeParseException e) {
            luongthuong_datengaychitra.setValue(null);
        }
    }


    // Hàm hỗ trợ

    // Dọn sạch form sau khi thêm hoặc sửa
    private void clearInputFields() {
        luongthuong_txmaluong.clear();
        luongthuong_txmaNV.clear();
        luongthuong_txluongcoban.clear();
        luongthuong_txphucap.clear();
        luongthuong_txthuong.clear();
        luongthuong_txkhautru.clear();
        luongthuong_txtongluong.clear();
        luongthuong_datethangnam.setValue(null);
        luongthuong_datengaychitra.setValue(null);
        luongthuong_tbluongthuong.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
    }

    
    // Các nút chức năng chính 
    
    // Thêm bản ghi lương mới
    @FXML
    private void luongthuong_themAction() {
        try {
            // Lấy dữ liệu từ form
            String maLuong = safeTrim(luongthuong_txmaluong.getText());
            String maNV = safeTrim(luongthuong_txmaNV.getText());
            LocalDate thangNamDate = luongthuong_datethangnam.getValue();
            LocalDate ngayChiTraDate = luongthuong_datengaychitra.getValue();

            // Kiểm tra các ô bắt buộc
            if (maLuong.isEmpty() || maNV.isEmpty() || thangNamDate == null || ngayChiTraDate == null) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập đủ mã lương, mã nhân viên, tháng năm và ngày chi trả!");
                return;
            }

            // Parse và kiểm tra giá trị tiền tệ
            double luongCoBan = parseMoneySafe(luongthuong_txluongcoban.getText(), "Lương cơ bản");
            double phuCap = parseMoneySafe(luongthuong_txphucap.getText(), "Phụ cấp");
            double thuong = parseMoneySafe(luongthuong_txthuong.getText(), "Thưởng");
            double khauTru = parseMoneySafe(luongthuong_txkhautru.getText(), "Khấu trừ");
            
            // Các điều kiện kiểm tra hợp lệ
            if (luongCoBan < 0 || phuCap < 0 || thuong < 0 || khauTru < 0) {
                canhbao.canhbao("Giá trị không hợp lệ", "Không được nhập số âm cho lương hoặc thưởng!");
                return;
            }
            if (khauTru > (luongCoBan + phuCap + thuong)) {
                canhbao.canhbao("Khấu trừ quá cao", "Khấu trừ không thể vượt tổng thu nhập!");
                return;
            }

            // Ngày chi trả không thể trước tháng lương
            if (ngayChiTraDate.isBefore(thangNamDate.withDayOfMonth(1))) {
                canhbao.canhbao("Lỗi thời gian", "Ngày chi trả không thể trước tháng lương!");
                return;
            }

            // Kiểm tra nhân viên có tồn tại không
            boolean tonTaiNV = DataService.getInstance().getDsNhanSu()
                    .stream().anyMatch(ns -> ns.getMaNV().equalsIgnoreCase(maNV));
            if (!tonTaiNV) {
                canhbao.canhbao("Sai mã nhân viên", "Không tìm thấy mã nhân viên '" + maNV + "'.");
                return;
            }

            // Kiểm tra trùng mã lương
            boolean tonTaiLuong = DataService.getInstance().getDsLuongThuong()
                    .stream().anyMatch(l -> l.getMaLuong().equalsIgnoreCase(maLuong));
            if (tonTaiLuong) {
                canhbao.canhbao("Trùng mã lương", "Mã lương '" + maLuong + "' đã tồn tại.");
                return;
            }

            // Tạo đối tượng mới và lưu xuống database
            String thangNam = thangNamDate.format(MONTH_YEAR);
            String ngayChiTra = ngayChiTraDate.format(YMD);
            LuongThuong lt = new LuongThuong(maLuong, maNV, thangNam, luongCoBan, phuCap, thuong, khauTru, ngayChiTra);

            // === Ghi xuống db ===
            boolean success = false;
            try {
                success = DataService.getInstance().addLuongThuong(lt);
            } catch (Exception e) {
                canhbao.canhbao("Lỗi khi thêm dữ liệu", "Không thể thêm lương thưởng: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            if (success) {
                refreshTable();
                canhbao.thongbao("Thành công 🎉", "Đã thêm lương thưởng cho nhân viên " + maNV);
                clearInputFields();
            } else {
                canhbao.canhbao("Không thành công", "Thêm thất bại. Kiểm tra dữ liệu và thử lại.");
            }

        } catch (Exception e) {
            canhbao.canhbao("Lỗi hệ thống", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // Nút "Xóa"
    @FXML
    private void luongthuong_xoaAction() {
        try {
            LuongThuong sel = luongthuong_tbluongthuong.getSelectionModel().getSelectedItem();
            if (sel == null) {
                canhbao.canhbao("Thiếu lựa chọn", "Vui lòng chọn dòng cần xóa!");
                return;
            }
            if (!canhbao.xacNhan("Xóa", "Xác nhận xóa", "Xóa mã lương: " + sel.getMaLuong() + "?")) return;
            DataService.getInstance().deleteLuongThuong(sel);
            refreshTable();
            clearInputFields();
            canhbao.thongbao("Đã xóa", "Bản ghi lương thưởng đã được xóa!");
        } catch (Exception e) {
            canhbao.canhbao("Lỗi xóa", "Không thể xóa bản ghi: " + e.getMessage());
        }
    }
    
    // Nút "Sửa" => mở cửa sổ chỉnh sửa riêng
    @FXML
    private void luongthuong_suaAction() {
        try {
            LuongThuong sel = luongthuong_tbluongthuong.getSelectionModel().getSelectedItem(); // Lấy dòng người dùng đang chọn trong bảng
            if (sel == null) { // Nếu chưa chọn dòng nào thì báo lỗi
                canhbao.canhbao("Thiếu lựa chọn", "Vui lòng chọn dòng cần sửa!");
                return;
            }
            
            // Tải file giao diện "sualuongthuong.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sualuongthuong.fxml"));
            Parent root = loader.load();
            SuaLuongThuong controller = loader.getController();  // Lấy controller của cửa sổ "Sửa lương thưởng"
            controller.setLuongThuong(sel); // Truyền đối tượng lương thưởng đang chọn sang cửa sổ sửa

            // Tạo cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Sửa lương thưởng");
            stage.setScene(new Scene(root));
            stage.show();
            // Sau khi đóng cửa sổ sửa → tải lại bảng dữ liệu
            stage.setOnHidden(e -> refreshTable());
        
        } catch (IOException e) {
            canhbao.canhbao("Lỗi giao diện", "Không thể mở form sửa: " + e.getMessage());
        } catch (Exception e) {
            canhbao.canhbao("Lỗi hệ thống", "Không thể mở cửa sổ sửa: " + e.getMessage());
        }
    }

    // Nút "Tìm kiếm" => mở cửa sổ riêng
    @FXML
    private void luongthuong_timkiemAction() {
        try {
            // Mở giao diện tìm kiếm "TimKiemLuongThuong.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TimKiemLuongThuong.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Tìm kiếm lương thưởng");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            canhbao.canhbao("Lỗi FXML", "Không thể mở cửa sổ tìm kiếm: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            canhbao.canhbao("Lỗi hệ thống", "Đã xảy ra lỗi khi mở chức năng tìm kiếm!");
            e.printStackTrace();
        }
    }

    // Nút "Xuất thông tin" => xuất file excel toàn bộ bản ghi lương thưởng
    @FXML
    private void luongthuong_xuatAction() {
        // Hỏi người dùng có muốn xuất dữ liệu hay không
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xuất Excel");
        confirm.setHeaderText("Bạn có muốn xuất toàn bộ dữ liệu lương thưởng ra file Excel?");
        confirm.setContentText("Hệ thống sẽ tạo file .xlsx chứa tất cả thông tin hiện có.");

        ButtonType yesBtn = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yesBtn, cancelBtn);
        confirm.showAndWait();
        // Nếu người dùng chọn Hủy → dừng lại
        if (confirm.getResult() == cancelBtn) {
            canhbao.thongbao("Đã hủy", "Không có dữ liệu nào được xuất.");
            return;
        }

        // Kiểm tra xem có dữ liệu để xuất không
        if (dsLuong == null || dsLuong.isEmpty()) {
            canhbao.canhbao("Không có dữ liệu", "Không có bản ghi lương thưởng nào để xuất!");
            return;
        }

        // Hiển thị hộp thoại cho người dùng chọn đường dẫn lưu file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn nơi lưu file Excel");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel File (*.xlsx)", "*.xlsx"));
        fileChooser.setInitialFileName("ThongTinLuongThuong.xlsx");
        // Người dùng chọn file đích
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) return;  // Nếu không chọn => dừng

        // Hiện cửa sổ progress trong lúc ghi file
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Đang xuất file...");
        progressAlert.setHeaderText("Hệ thống đang tạo file Excel, vui lòng chờ...");
        progressAlert.getDialogPane().setContent(progressIndicator);
        progressAlert.show();

        // Tạo task chạy nền để không bị treo giao diện
        Task<Void> exportTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                exportLuongThuongToExcel(file);
                return null;
            }
        };

        // Khi task hoàn thành => hiện thông báo thành công
        exportTask.setOnSucceeded(e -> {
            progressAlert.close();
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Xuất thành công!");
            success.setHeaderText("Đã xuất dữ liệu lương thưởng thành công!");
            success.setContentText("File đã lưu tại:\n" + file.getAbsolutePath());

            ButtonType openBtn = new ButtonType("Mở file");
            ButtonType backBtn = new ButtonType("Quay lại");
            success.getButtonTypes().setAll(openBtn, backBtn);
            success.showAndWait();

            // Nếu người dùng chọn “Mở file” => tự mở file bằng Excel
            if (success.getResult() == openBtn) {
                try {
                    java.awt.Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    canhbao.canhbao("Lỗi", "Không thể mở file vừa tạo.");
                }
            }
        });

        // Nếu task gặp lỗi => đóng progress và báo lỗi
        exportTask.setOnFailed(e -> {
            progressAlert.close();
            canhbao.canhbao("Lỗi", "Không thể xuất file Excel.\nChi tiết: " + exportTask.getException());
        });
        
        // Chạy task trong thread riêng để không bị treo UI
        new Thread(exportTask).start();
    }

    // Nút "Quay lại" => về màn hình chính (main.fxml)
    @FXML
    private void luongthuong_quaylaiAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) luongthuong_btquaylai.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    
    // Các hàm hỗ trợ 
    
    // Hàm ghi file Excel toàn bộ lương thưởng 
    private void exportLuongThuongToExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("LuongThuong");

            // Tạo dòng tiêu đề
            Row header = sheet.createRow(0);
            String[] headers = {
                "Mã lương", "Mã nhân viên", "Tháng/Năm", "Lương cơ bản",
                "Phụ cấp", "Thưởng", "Khấu trừ", "Tổng lương", "Ngày chi trả"
            };
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Ghi từng bản ghi dữ liệu xuống file
            int rowNum = 1;
            for (LuongThuong lt : dsLuong) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(lt.getMaLuong());
                row.createCell(1).setCellValue(lt.getMaNhanVien());
                row.createCell(2).setCellValue(lt.getThangNam());
                row.createCell(3).setCellValue(lt.getLuongCoBan());
                row.createCell(4).setCellValue(lt.getPhuCap());
                row.createCell(5).setCellValue(lt.getThuong());
                row.createCell(6).setCellValue(lt.getKhauTru());
                row.createCell(7).setCellValue(lt.getTongLuong());
                row.createCell(8).setCellValue(lt.getNgayChiTra());
            }

            // Tự động căn rộng cột cho đẹp
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            //  Ghi workbook ra file Excel thật trên đĩa
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }
    
     // Cắt chuỗi an toàn, tránh lỗi null
    private String safeTrim(String text) {
        return text == null ? "" : text.trim();
    }

    // Chuyển chuỗi thành số, nếu lỗi thì hiện cảnh báo
    private double parseMoneySafe(String s, String fieldName) {
        try {
            return parseMoney(s);
        } catch (NumberFormatException e) {
            canhbao.canhbao("Giá trị không hợp lệ", "Ô \"" + fieldName + "\" phải là số nguyên hợp lệ!");
            return -1;
        }
    }

    // Chỉ cho phép nhập số nguyên dương, chặn ký tự lạ (chấm, phẩy, chữ...)
    private void addIntegerOnlyValidation(TextField field, String fieldName) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
            if (!newValue.matches("\\d+")) {
                canhbao.canhbao("Giá trị không hợp lệ",
                        "Ô \"" + fieldName + "\" chỉ được phép nhập số nguyên dương!\nKhông dùng dấu phẩy, chấm hay ký tự khác.");
                field.setText(oldValue);
            }
        });
    }
}
