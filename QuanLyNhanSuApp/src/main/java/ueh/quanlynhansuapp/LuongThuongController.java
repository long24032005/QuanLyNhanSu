package ueh.quanlynhansuapp;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller quản lý chức năng lương thưởng.
 * Phiên bản đã được cập nhật để bao phủ hầu hết ngoại lệ có thể xảy ra khi sinh viên thao tác nhập liệu và xử lý DB.
 */
public class LuongThuongController {

    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    
    

    private final ObservableList<LuongThuong> dsLuong = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // ==== BINDING CỘT ====
            luongthuong_colmaluong.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaLuong()));
            luongthuong_colmaNV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaNhanVien()));
            luongthuong_colthangnam.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getThangNam()));
            luongthuong_colngaychitra.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNgayChiTra()));

            luongthuong_colluongcoban.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
            luongthuong_colphucap.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPhuCap()));
            luongthuong_colthuong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getThuong()));
            luongthuong_colkhautru.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getKhauTru()));
            luongthuong_coltongluong.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTongLuong()));

            alignRight(luongthuong_colluongcoban);
            alignRight(luongthuong_colphucap);
            alignRight(luongthuong_colthuong);
            alignRight(luongthuong_colkhautru);
            alignRight(luongthuong_coltongluong);

            // ==== LOAD DỮ LIỆU ====
            DataService.getInstance().reloadAllData();
            dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
            luongthuong_tbluongthuong.setItems(dsLuong);

            // ==== AUTO FILL KHI CHỌN DÒNG ====
            luongthuong_tbluongthuong.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, lt) -> {
                if (lt == null) return;
                try {
                    fillForm(lt);
                } catch (Exception ex) {
                    canhbao.canhbao("Lỗi hiển thị", "Không thể hiển thị chi tiết dòng: " + ex.getMessage());
                }
            });

            // ==== AUTO TÍNH TỔNG LƯƠNG ====
            ChangeListener<String> recalc = (o, a, b) -> capNhatTongLuong();
            luongthuong_txluongcoban.textProperty().addListener(recalc);
            luongthuong_txphucap.textProperty().addListener(recalc);
            luongthuong_txthuong.textProperty().addListener(recalc);
            luongthuong_txkhautru.textProperty().addListener(recalc);

            luongthuong_txtongluong.setEditable(false);
            luongthuong_txtongluong.setStyle("-fx-alignment: CENTER-LEFT");

            // ==== CHỈ CHO PHÉP NHẬP SỐ NGUYÊN DƯƠNG ====
            addIntegerOnlyValidation(luongthuong_txluongcoban, "Lương cơ bản");
            addIntegerOnlyValidation(luongthuong_txphucap, "Phụ cấp");
            addIntegerOnlyValidation(luongthuong_txthuong, "Thưởng");
            addIntegerOnlyValidation(luongthuong_txkhautru, "Khấu trừ");

        } catch (Exception e) {
            canhbao.canhbao("Lỗi khởi tạo", "Không thể khởi tạo bảng dữ liệu:\n" + e.getMessage());
        }
        
        // ==== Tùy chỉnh DatePicker "Tháng/Năm" ====
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

    // =========================== HỖ TRỢ =============================

    private void alignRight(TableColumn<LuongThuong, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : doubleToPlain(item));
                setStyle("-fx-alignment: CENTER-LEFT;");
            }
        });
    }

    private String doubleToPlain(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9) return String.valueOf((long)Math.rint(v));
        return String.format("%.2f", v);
    }

    private double parseMoney(String s) throws NumberFormatException {
        if (s == null || s.isBlank()) return 0;
        return Double.parseDouble(s.trim());
    }

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

    private void fillForm(LuongThuong lt) {
        luongthuong_txmaluong.setText(lt.getMaLuong());
        luongthuong_txmaNV.setText(lt.getMaNhanVien());
        luongthuong_txluongcoban.setText(doubleToPlain(lt.getLuongCoBan()));
        luongthuong_txphucap.setText(doubleToPlain(lt.getPhuCap()));
        luongthuong_txthuong.setText(doubleToPlain(lt.getThuong()));
        luongthuong_txkhautru.setText(doubleToPlain(lt.getKhauTru()));
        luongthuong_txtongluong.setText(doubleToPlain(lt.getTongLuong()));

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


    // ============= HÀM HỖ TRỢ =============

    /** 🧹 Dọn form sau khi thêm hoặc sửa */
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

    // =========================== CÁC NÚT CHỨC NĂNG =============================

    @FXML
    private void luongthuong_themAction() {
        try {
            String maLuong = safeTrim(luongthuong_txmaluong.getText());
            String maNV = safeTrim(luongthuong_txmaNV.getText());
            LocalDate thangNamDate = luongthuong_datethangnam.getValue();
            LocalDate ngayChiTraDate = luongthuong_datengaychitra.getValue();

            // === Kiểm tra thiếu dữ liệu ===
            if (maLuong.isEmpty() || maNV.isEmpty() || thangNamDate == null || ngayChiTraDate == null) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập đủ mã lương, mã nhân viên, tháng năm và ngày chi trả!");
                return;
            }

            // === Parse và validate tiền tệ ===
            double luongCoBan = parseMoneySafe(luongthuong_txluongcoban.getText(), "Lương cơ bản");
            double phuCap = parseMoneySafe(luongthuong_txphucap.getText(), "Phụ cấp");
            double thuong = parseMoneySafe(luongthuong_txthuong.getText(), "Thưởng");
            double khauTru = parseMoneySafe(luongthuong_txkhautru.getText(), "Khấu trừ");

            if (luongCoBan < 0 || phuCap < 0 || thuong < 0 || khauTru < 0) {
                canhbao.canhbao("Giá trị không hợp lệ", "Không được nhập số âm cho lương hoặc thưởng!");
                return;
            }
            if (khauTru > (luongCoBan + phuCap + thuong)) {
                canhbao.canhbao("Khấu trừ quá cao", "Khấu trừ không thể vượt tổng thu nhập!");
                return;
            }

            // === Kiểm tra logic ngày ===
            if (ngayChiTraDate.isBefore(thangNamDate.withDayOfMonth(1))) {
                canhbao.canhbao("Lỗi thời gian", "Ngày chi trả không thể trước tháng lương!");
                return;
            }

            // === Kiểm tra nhân viên tồn tại ===
            boolean tonTaiNV = DataService.getInstance().getDsNhanSu()
                    .stream().anyMatch(ns -> ns.getMaNV().equalsIgnoreCase(maNV));
            if (!tonTaiNV) {
                canhbao.canhbao("Sai mã nhân viên", "Không tìm thấy mã nhân viên '" + maNV + "'.");
                return;
            }

            // === Kiểm tra trùng mã lương ===
            boolean tonTaiLuong = DataService.getInstance().getDsLuongThuong()
                    .stream().anyMatch(l -> l.getMaLuong().equalsIgnoreCase(maLuong));
            if (tonTaiLuong) {
                canhbao.canhbao("Trùng mã lương", "Mã lương '" + maLuong + "' đã tồn tại.");
                return;
            }

            // === Tạo đối tượng ===
            String thangNam = thangNamDate.format(MONTH_YEAR);
            String ngayChiTra = ngayChiTraDate.format(YMD);
            LuongThuong lt = new LuongThuong(maLuong, maNV, thangNam, luongCoBan, phuCap, thuong, khauTru, ngayChiTra);

            // === Ghi xuống DB ===
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
            e.printStackTrace();
        }
    }

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

    @FXML
    private void luongthuong_suaAction() {
        try {
            LuongThuong sel = luongthuong_tbluongthuong.getSelectionModel().getSelectedItem();
            if (sel == null) {
                canhbao.canhbao("Thiếu lựa chọn", "Vui lòng chọn dòng cần sửa!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("sualuongthuong.fxml"));
            Parent root = loader.load();
            SuaLuongThuong controller = loader.getController();
            controller.setLuongThuong(sel);

            Stage stage = new Stage();
            stage.setTitle("Sửa lương thưởng");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            canhbao.canhbao("Lỗi giao diện", "Không thể mở form sửa: " + e.getMessage());
        } catch (Exception e) {
            canhbao.canhbao("Lỗi hệ thống", "Không thể mở cửa sổ sửa: " + e.getMessage());
        }
    }

    @FXML
    private void luongthuong_timkiemAction() {
        try {
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

    @FXML
    private void luongthuong_xuatAction() {
        try {
            // 1️⃣ Xác nhận trước khi xuất
            boolean xacNhan = canhbao.xacNhan(
                "Xác nhận xuất dữ liệu",
                "Xuất toàn bộ thông tin lương thưởng",
                "Bạn có muốn xuất toàn bộ thông tin lương thưởng ra file Excel không?"
            );

            if (!xacNhan) {
                return; // Người dùng chọn Cancel
            }

            // 2️⃣ Kiểm tra dữ liệu
            if (dsLuong == null || dsLuong.isEmpty()) {
                canhbao.canhbao("Không có dữ liệu", "Không có bản ghi nào để xuất!");
                return;
            }

            // 3️⃣ Hộp thoại chọn nơi lưu file
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Chọn nơi lưu file Excel");
            fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx")
            );

            java.io.File file = fileChooser.showSaveDialog(luongthuong_tbluongthuong.getScene().getWindow());
            if (file == null) {
                return; // người dùng đóng hộp thoại
            }

            // 4️⃣ Tạo workbook và sheet
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("LuongThuong");

            // 5️⃣ Style header
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);

            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);

            // Style cho số
            org.apache.poi.ss.usermodel.CellStyle numStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.DataFormat format = workbook.createDataFormat();
            numStyle.setDataFormat(format.getFormat("#,##0"));
            numStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);

            // 6️⃣ Ghi hàng tiêu đề
            String[] headers = {
                "Mã lương", "Mã nhân viên", "Tháng năm", "Lương cơ bản",
                "Phụ cấp", "Thưởng", "Khấu trừ", "Tổng lương", "Ngày chi trả"
            };

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 7️⃣ Ghi dữ liệu từng dòng
            int rowNum = 1;
            for (LuongThuong lt : dsLuong) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(lt.getMaLuong());
                row.createCell(1).setCellValue(lt.getMaNhanVien());
                row.createCell(2).setCellValue(lt.getThangNam());

                org.apache.poi.ss.usermodel.Cell c3 = row.createCell(3);
                c3.setCellValue(lt.getLuongCoBan());
                c3.setCellStyle(numStyle);

                org.apache.poi.ss.usermodel.Cell c4 = row.createCell(4);
                c4.setCellValue(lt.getPhuCap());
                c4.setCellStyle(numStyle);

                org.apache.poi.ss.usermodel.Cell c5 = row.createCell(5);
                c5.setCellValue(lt.getThuong());
                c5.setCellStyle(numStyle);

                org.apache.poi.ss.usermodel.Cell c6 = row.createCell(6);
                c6.setCellValue(lt.getKhauTru());
                c6.setCellStyle(numStyle);

                org.apache.poi.ss.usermodel.Cell c7 = row.createCell(7);
                c7.setCellValue(lt.getTongLuong());
                c7.setCellStyle(numStyle);

                row.createCell(8).setCellValue(lt.getNgayChiTra());
            }

            // 8️⃣ Auto resize cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 9️⃣ Ghi file ra đĩa
            try (java.io.FileOutputStream out = new java.io.FileOutputStream(file)) {
                workbook.write(out);
            }

            workbook.close();

            // 🔟 Thông báo thành công
            canhbao.thongbao("Xuất thành công 🎉", "Đã xuất dữ liệu ra file Excel:\n" + file.getAbsolutePath());

        } catch (java.io.FileNotFoundException e) {
            canhbao.canhbao("Lỗi ghi file", "Không thể ghi file (file đang được mở hoặc bị khóa)!");
        } catch (Exception e) {
            e.printStackTrace();
            canhbao.canhbao("Lỗi hệ thống", "Đã xảy ra lỗi khi xuất dữ liệu:\n" + e.getMessage());
        }
    }

    @FXML
    private void luongthuong_quaylaiAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) luongthuong_btquaylai.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    private String safeTrim(String text) {
        return text == null ? "" : text.trim();
    }

    private double parseMoneySafe(String s, String fieldName) {
        try {
            return parseMoney(s);
        } catch (NumberFormatException e) {
            canhbao.canhbao("Giá trị không hợp lệ", "Ô \"" + fieldName + "\" phải là số nguyên hợp lệ!");
            return -1;
        }
    }

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
