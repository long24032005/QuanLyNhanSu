package ueh.quanlynhansuapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller cho màn hình "Sửa lương thưởng"
 * Chuẩn hóa: Bao phủ ngoại lệ, kiểm tra hợp lệ, auto tính tổng lương.
 */
public class SuaLuongThuong {

    // ===== Định dạng ngày dùng chung =====
    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===== Biến =====
    private LuongThuong luongThuong;

    // ====== FXML Components ======
    @FXML private TextField sualuongthuong_txmaluong;
    @FXML private TextField sualuongthuong_txmaNV;
    @FXML private DatePicker sualuongthuong_datethangnam;
    @FXML private TextField sualuongthuong_txluongcoban;
    @FXML private TextField sualuongthuong_txphucap;
    @FXML private TextField sualuongthuong_txthuong;
    @FXML private TextField sualuongthuong_txkhautru;
    @FXML private TextField sualuongthuong_txtongluong;
    @FXML private DatePicker sualuongthuong_datengaychitra;
    @FXML private Button sualuongthuong_btsua;
    @FXML private Button sualuongthuong_btquaylai;

    // ==================== KHỞI TẠO ====================

    @FXML
    public void initialize() {
        try {
            // Không cho sửa mã lương (khóa chính)
            sualuongthuong_txmaluong.setEditable(false);
            sualuongthuong_txtongluong.setEditable(false);
            sualuongthuong_txtongluong.setStyle("-fx-alignment: CENTER-LEFT;");

            // Auto tính tổng lương khi nhập
            ChangeListener<String> recalc = (o, a, b) -> capNhatTongLuong();
            sualuongthuong_txluongcoban.textProperty().addListener(recalc);
            sualuongthuong_txphucap.textProperty().addListener(recalc);
            sualuongthuong_txthuong.textProperty().addListener(recalc);
            sualuongthuong_txkhautru.textProperty().addListener(recalc);

            // Ràng buộc: chỉ cho phép nhập số nguyên dương
            addIntegerOnlyValidation(sualuongthuong_txluongcoban, "Lương cơ bản");
            addIntegerOnlyValidation(sualuongthuong_txphucap, "Phụ cấp");
            addIntegerOnlyValidation(sualuongthuong_txthuong, "Thưởng");
            addIntegerOnlyValidation(sualuongthuong_txkhautru, "Khấu trừ");
        } catch (Exception e) {
            canhbao.canhbao("Lỗi khởi tạo", "Không thể khởi tạo form sửa: " + e.getMessage());
        }
    }

    // ==================== NHẬN DỮ LIỆU TỪ FORM CHÍNH ====================

    public void setLuongThuong(LuongThuong lt) {
        try {
            this.luongThuong = lt;
            if (lt == null) return;

            sualuongthuong_txmaluong.setText(lt.getMaLuong());
            sualuongthuong_txmaNV.setText(lt.getMaNhanVien());
            sualuongthuong_txluongcoban.setText(doubleToPlain(lt.getLuongCoBan()));
            sualuongthuong_txphucap.setText(doubleToPlain(lt.getPhuCap()));
            sualuongthuong_txthuong.setText(doubleToPlain(lt.getThuong()));
            sualuongthuong_txkhautru.setText(doubleToPlain(lt.getKhauTru()));
            sualuongthuong_txtongluong.setText(doubleToPlain(lt.getTongLuong()));

            try {
                sualuongthuong_datethangnam.setValue(LocalDate.parse("01/" + lt.getThangNam(), DMY));
            } catch (DateTimeParseException ex) {
                sualuongthuong_datethangnam.setValue(null);
            }

            try {
                String ngay = lt.getNgayChiTra();
                LocalDate d = ngay.contains("/") ? LocalDate.parse(ngay, DMY) : LocalDate.parse(ngay, YMD);
                sualuongthuong_datengaychitra.setValue(d);
            } catch (DateTimeParseException ex) {
                sualuongthuong_datengaychitra.setValue(null);
            }

        } catch (Exception e) {
            canhbao.canhbao("Lỗi dữ liệu", "Không thể hiển thị thông tin lương: " + e.getMessage());
        }
    }

    // ==================== NÚT SỬA ====================

    @FXML
    private void sualuongthuong_suaAction() {
        try {
            if (luongThuong == null) {
                canhbao.canhbao("Lỗi hệ thống", "Không tìm thấy bản ghi cần sửa!");
                return;
            }

            String maLuong = safeTrim(sualuongthuong_txmaluong.getText());
            String maNV = safeTrim(sualuongthuong_txmaNV.getText());
            LocalDate thangNamDate = sualuongthuong_datethangnam.getValue();
            LocalDate ngayChiTraDate = sualuongthuong_datengaychitra.getValue();

            // ===== Kiểm tra thiếu dữ liệu =====
            if (maNV.isEmpty() || thangNamDate == null || ngayChiTraDate == null) {
                canhbao.canhbao("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin trước khi lưu!");
                return;
            }

            // ===== Parse tiền =====
            double luongCoBan = parseMoneySafe(sualuongthuong_txluongcoban.getText(), "Lương cơ bản");
            double phuCap = parseMoneySafe(sualuongthuong_txphucap.getText(), "Phụ cấp");
            double thuong = parseMoneySafe(sualuongthuong_txthuong.getText(), "Thưởng");
            double khauTru = parseMoneySafe(sualuongthuong_txkhautru.getText(), "Khấu trừ");

            if (luongCoBan < 0 || phuCap < 0 || thuong < 0 || khauTru < 0) {
                canhbao.canhbao("Giá trị không hợp lệ", "Không được nhập số âm cho các trường lương!");
                return;
            }
            if (khauTru > (luongCoBan + phuCap + thuong)) {
                canhbao.canhbao("Khấu trừ quá cao", "Khoản khấu trừ không thể vượt tổng thu nhập!");
                return;
            }

            // ===== Kiểm tra ngày =====
            if (ngayChiTraDate.isBefore(thangNamDate.withDayOfMonth(1))) {
                canhbao.canhbao("Lỗi thời gian", "Ngày chi trả không thể trước tháng lương!");
                return;
            }

            // ===== Kiểm tra nhân viên có tồn tại =====
            boolean tonTaiNV = DataService.getInstance().getDsNhanSu()
                    .stream().anyMatch(ns -> ns.getMaNV().equalsIgnoreCase(maNV));
            if (!tonTaiNV) {
                canhbao.canhbao("Sai mã nhân viên", "Không tìm thấy mã nhân viên '" + maNV + "' trong hệ thống!");
                return;
            }

            // ===== Xác nhận =====
            if (!canhbao.xacNhan("Xác nhận sửa", "Lưu thay đổi?", 
                    "Mã lương: " + maLuong + "\nNhân viên: " + maNV)) {
                return;
            }

            // ===== Cập nhật vào đối tượng =====
            luongThuong.setMaNhanVien(maNV);
            luongThuong.setThangNam(thangNamDate.format(MONTH_YEAR));
            luongThuong.setLuongCoBan(luongCoBan);
            luongThuong.setPhuCap(phuCap);
            luongThuong.setThuong(thuong);
            luongThuong.setKhauTru(khauTru);
            luongThuong.setNgayChiTra(ngayChiTraDate.format(YMD));

            // ===== Lưu xuống DB =====
            try {
                DataService.getInstance().updateLuongThuong(luongThuong);
                canhbao.thongbao("Thành công 🎉", "Đã cập nhật thông tin lương thưởng!");
                ((Stage) sualuongthuong_btsua.getScene().getWindow()).close();
            } catch (Exception ex) {
                canhbao.canhbao("Lỗi cơ sở dữ liệu", "Không thể cập nhật dữ liệu: " + ex.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            canhbao.canhbao("Lỗi hệ thống", "Đã xảy ra lỗi khi sửa: " + e.getMessage());
        }
    }

    // ==================== NÚT QUAY LẠI ====================

    @FXML
    private void sualuongthuong_quaylaiAction() {
        try {
            ((Stage) sualuongthuong_btquaylai.getScene().getWindow()).close();
        } catch (Exception e) {
            canhbao.canhbao("Lỗi thoát", "Không thể đóng cửa sổ: " + e.getMessage());
        }
    }

    // ==================== HÀM HỖ TRỢ ====================

    private void capNhatTongLuong() {
        try {
            double luongCoBan = parseMoneySafe(sualuongthuong_txluongcoban.getText(), "");
            double phuCap = parseMoneySafe(sualuongthuong_txphucap.getText(), "");
            double thuong = parseMoneySafe(sualuongthuong_txthuong.getText(), "");
            double khauTru = parseMoneySafe(sualuongthuong_txkhautru.getText(), "");
            double tong = luongCoBan + phuCap + thuong - khauTru;
            if (tong < 0) tong = 0;
            sualuongthuong_txtongluong.setText(doubleToPlain(tong));
        } catch (Exception ignored) {
            sualuongthuong_txtongluong.clear();
        }
    }

    private String doubleToPlain(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9) return String.valueOf((long)Math.rint(v));
        return String.format("%.2f", v);
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private double parseMoneySafe(String s, String fieldName) {
        try {
            if (s == null || s.isBlank()) return 0;
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            if (!fieldName.isEmpty()) {
                canhbao.canhbao("Giá trị không hợp lệ", "Ô \"" + fieldName + "\" phải là số hợp lệ!");
            }
            return -1;
        }
    }

    private void addIntegerOnlyValidation(TextField field, String fieldName) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
            if (!newValue.matches("\\d+")) {
                canhbao.canhbao("Giá trị không hợp lệ",
                        "Ô \"" + fieldName + "\" chỉ được phép nhập số nguyên dương!");
                field.setText(oldValue);
            }
        });
    }
}
