/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.cell.TextFieldTableCell;
import java.text.DecimalFormat;

/**
 * Controller quản lý chức năng lương thưởng.
 * Hỗ trợ thêm, sửa, xóa, tìm kiếm, xuất thông tin.
 */
public class LuongThuongController {

    // ====== Định dạng ngày dùng chung ======
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ====== Các TextField & DatePicker ======
    @FXML private TextField luongthuong_txmaluong;
    @FXML private TextField luongthuong_txmaNV;
    @FXML private DatePicker luongthuong_datethangnam;
    @FXML private TextField luongthuong_txluongcoban;
    @FXML private TextField luongthuong_txphucap;
    @FXML private TextField luongthuong_txthuong;
    @FXML private TextField luongthuong_txkhautru;
    @FXML private TextField luongthuong_txtongluong;
    @FXML private DatePicker luongthuong_datengaychitra;

    // ====== Bảng dữ liệu và các cột ======
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

    // ====== Dữ liệu hiển thị ======
    private final ObservableList<LuongThuong> dsLuong = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DecimalFormat df = new DecimalFormat("#,###.##");
        // Liên kết dữ liệu với cột
        luongthuong_colmaluong.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaLuong()));
        luongthuong_colmaNV.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaNhanVien()));
        luongthuong_colthangnam.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getThangNam()));
        luongthuong_colngaychitra.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNgayChiTra()));
        // Lương cơ bản
        luongthuong_colluongcoban.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getLuongCoBan()));
        luongthuong_colluongcoban.setCellFactory(col -> new TableCell<LuongThuong, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Phụ cấp
        luongthuong_colphucap.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPhuCap()));
        luongthuong_colphucap.setCellFactory(col -> new TableCell<LuongThuong, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Thưởng
        luongthuong_colthuong.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getThuong()));
        luongthuong_colthuong.setCellFactory(col -> new TableCell<LuongThuong, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Khấu trừ
        luongthuong_colkhautru.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getKhauTru()));
        luongthuong_colkhautru.setCellFactory(col -> new TableCell<LuongThuong, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Tổng lương
        luongthuong_coltongluong.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTongLuong()));
        luongthuong_coltongluong.setCellFactory(col -> new TableCell<LuongThuong, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Load dữ liệu ban đầu từ DB
        DataService.getInstance().reloadAllData();
        dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
        luongthuong_tbluongthuong.setItems(dsLuong);

        // 🧮 Tự động tính tổng lương khi người dùng nhập các ô số
        luongthuong_tbluongthuong.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {             
                luongthuong_txmaluong.setText(newVal.getMaLuong());
                luongthuong_txmaNV.setText(newVal.getMaNhanVien());
                luongthuong_txluongcoban.setText(df.format(newVal.getLuongCoBan()));
                luongthuong_txphucap.setText(df.format(newVal.getPhuCap()));
                luongthuong_txthuong.setText(df.format(newVal.getThuong()));
                luongthuong_txkhautru.setText(df.format(newVal.getKhauTru()));
                luongthuong_txtongluong.setText(df.format(newVal.getTongLuong())); // ✅ Không còn E7 nữa

                try {
                    luongthuong_datethangnam.setValue(LocalDate.parse("01/" + newVal.getThangNam(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } catch (Exception e) {
                    luongthuong_datethangnam.setValue(null);
                }

                try {
                    String ngay = newVal.getNgayChiTra();
                    LocalDate d = ngay.contains("/") ?
                            LocalDate.parse(ngay, DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                            LocalDate.parse(ngay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    luongthuong_datengaychitra.setValue(d);
                } catch (Exception e) {
                    luongthuong_datengaychitra.setValue(null);
                }
            }
        });
    }

    // ============= CÁC NÚT CHỨC NĂNG =============

     /* Nút “Thêm” */
    @FXML
    private void luongthuong_themAction() {
        try {
            // ====== Lấy dữ liệu ======
            String maLuong = luongthuong_txmaluong.getText().trim();
            String maNV = luongthuong_txmaNV.getText().trim();
            LocalDate thangNamDate = luongthuong_datethangnam.getValue();
            LocalDate ngayChiTraDate = luongthuong_datengaychitra.getValue();

            if (maLuong.isEmpty() || maNV.isEmpty() || thangNamDate == null || ngayChiTraDate == null) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin bắt buộc (*) !");
                return;
            }

            String thangNam = thangNamDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            String ngayChiTra = ngayChiTraDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            double luongCoBan, phuCap, thuong, khauTru;
            try {
                luongCoBan = Double.parseDouble(luongthuong_txluongcoban.getText().trim());
                phuCap = Double.parseDouble(luongthuong_txphucap.getText().trim());
                thuong = Double.parseDouble(luongthuong_txthuong.getText().trim());
                khauTru = Double.parseDouble(luongthuong_txkhautru.getText().trim());
            } catch (NumberFormatException e) {
                canhbao.canhbao("Lỗi định dạng",
                        "Một hoặc nhiều trường lương không hợp lệ!\n" +
                        "• Lương cơ bản, phụ cấp, thưởng, khấu trừ phải là SỐ.\n" +
                        "• Không nhập ký tự chữ hoặc dấu cách thừa.");
                return;
            }

            if (luongCoBan < 0 || phuCap < 0 || thuong < 0 || khauTru < 0) {
                canhbao.canhbao("Giá trị không hợp lệ",
                        "Vui lòng kiểm tra lại:\n" +
                        "• Lương, phụ cấp, thưởng, khấu trừ không được âm.");
                return;
            }

            if (khauTru > (luongCoBan + phuCap + thuong)) {
                canhbao.canhbao("Khấu trừ quá cao", "Khoản khấu trừ không thể vượt quá tổng thu nhập!");
                return;
            }

            // ====== Kiểm tra trong hệ thống ======
            boolean trungMa = DataService.getInstance().getDsLuongThuong()
                    .stream().anyMatch(lt -> lt.getMaLuong().equalsIgnoreCase(maLuong));
            
        
            if (trungMa) {
                canhbao.canhbao("Trùng mã lương", "Mã lương '" + maLuong + "' đã tồn tại trong hệ thống!");
                return;
            }

            boolean tonTaiNV = DataService.getInstance().getDsNhanSu()
                    .stream().anyMatch(ns -> ns.getMaNV().equalsIgnoreCase(maNV));
            if (!tonTaiNV) {
                canhbao.canhbao("Sai mã nhân viên",
                        "Không tìm thấy mã nhân viên '" + maNV + "' trong hệ thống.\n" +
                        "• Hãy kiểm tra lại danh sách nhân viên.\n" +
                        "• Nếu nhân viên mới, cần thêm vào mục 'Nhân sự'.\n" +
                         "trước khi tạo lương.");
                return;
            }

            // ====== Ghi dữ liệu xuống DB ======
            LuongThuong lt = new LuongThuong(maLuong, maNV, thangNam, luongCoBan, phuCap, thuong, khauTru, ngayChiTra);
            boolean success;
            try {
                success = DataService.getInstance().addLuongThuong(lt);
            } catch (Exception dbEx) {
                dbEx.printStackTrace();
                canhbao.canhbao("Lỗi cơ sở dữ liệu",
                        "Không thể ghi dữ liệu xuống bảng luongthuong.\n" +
                        "Nguyên nhân có thể:\n" +
                        "• Kết nối MySQL bị ngắt.\n" +
                        "• Cấu trúc bảng không đúng.\n" +
                        "• Dữ liệu đầu vào vượt giới hạn cột.");
                return;
            }

            if (success) {
                refreshTable();
                canhbao.thongbao("Thành công 🎉", "Đã thêm lương thưởng cho nhân viên " + maNV + " (" + thangNam + ")!");
                clearInputFields();
            } else {
                canhbao.canhbao("Thất bại",
                        "Không thể thêm bản ghi lương thưởng.\n" +
                        "• Có thể do lỗi ghi DB hoặc kết nối bị ngắt.\n" +
                        "• Vui lòng thử lại hoặc kiểm tra nhật ký MySQL.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            canhbao.canhbao("Lỗi hệ thống",
                    "Đã xảy ra lỗi ngoài ý muốn trong quá trình thêm mới:\n" +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    /** 🔴 Nút “Xóa” */
    @FXML
    private void luongthuong_xoaAction() {
        LuongThuong selected = luongthuong_tbluongthuong.getSelectionModel().getSelectedItem();
        if (selected == null) {
            canhbao.canhbao("Thiếu lựa chọn", "Vui lòng chọn dòng cần xóa!");
            return;
        }

        boolean xacNhan = canhbao.xacNhan("Xác nhận xóa", "Xóa bản ghi này?", 
                                          "Bạn có chắc muốn xóa mã lương: " + selected.getMaLuong() + " ?");
        if (!xacNhan) return;

        DataService.getInstance().deleteLuongThuong(selected);
        refreshTable();
        canhbao.thongbao("Đã xóa", "Bản ghi lương thưởng đã được xóa!");
    }
    /** 🔄 Nút “Sửa” */
    @FXML
    private void luongthuong_suaAction() {
        LuongThuong selected = luongthuong_tbluongthuong.getSelectionModel().getSelectedItem();
        if (selected == null) {
            canhbao.canhbao("Thiếu lựa chọn", "Vui lòng chọn một dòng để sửa!");
            return;
        }

        // Ví dụ: cho phép sửa trực tiếp trên form
        luongthuong_txmaluong.setText(selected.getMaLuong());
        luongthuong_txmaNV.setText(selected.getMaNhanVien());
        // bạn có thể thêm logic mở cửa sổ con sửa ở đây
        canhbao.thongbao("Thông tin", "Chức năng sửa đang được phát triển!");
    }
    /** 🔍 Nút “Tìm kiếm” */
    @FXML
    private void luongthuong_timkiemAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timkiemluongthuong.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Tìm kiếm lương thưởng");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** 📤 Nút “Xuất thông tin” */
    @FXML
    private void luongthuong_xuatAction() {
        canhbao.thongbao("Đang phát triển", "Chức năng xuất file Excel sẽ được cập nhật sau!");
    }

    /** ↩️ Nút “Quay lại” */
    @FXML
    private void luongthuong_quaylaiAction() {
        App.setRoot("main");
    }

    // ============= HÀM HỖ TRỢ =============

    /** 🧹 Dọn form sau khi thêm hoặc sửa */
    private void clearInputFields() {
        luongthuong_txmaluong.clear();
        luongthuong_txmaNV.clear();
        luongthuong_datethangnam.setValue(null);
        luongthuong_txluongcoban.clear();
        luongthuong_txphucap.clear();
        luongthuong_txthuong.clear();
        luongthuong_txkhautru.clear();
        luongthuong_txtongluong.clear();
        luongthuong_datengaychitra.setValue(null);
        luongthuong_tbluongthuong.getSelectionModel().clearSelection();
    }

    /** 🧮 Tự động tính tổng lương động */
    private void capNhatTongLuong() {
        try {
            double luongCoBan = Double.parseDouble(luongthuong_txluongcoban.getText().trim());
            double phuCap = Double.parseDouble(luongthuong_txphucap.getText().trim());
            double thuong = Double.parseDouble(luongthuong_txthuong.getText().trim());
            double khauTru = Double.parseDouble(luongthuong_txkhautru.getText().trim());
            double tongLuong = luongCoBan + phuCap + thuong - khauTru;
            luongthuong_txtongluong.setText(String.format("%,.0f", tongLuong)); // có dấu phẩy ngăn cách
        } catch (NumberFormatException e) {
            luongthuong_txtongluong.clear();
        }
    }

    /** 🔄 Làm mới lại bảng sau khi thêm/sửa/xóa */
    private void refreshTable() {
        dsLuong.setAll(DataService.getInstance().getDsLuongThuong());
    }
}