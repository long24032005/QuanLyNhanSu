/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author philo
 */
public class Database {
// Đường dẫn tới file database. Sẽ tự động tạo trong thư mục gốc của dự án nếu chưa có.
    // Tên file dữ liệu của chúng ta
    private static final String FILE_NAME = "quanlynhansu.xlsx";
    private static final String SHEET_PHONGBAN = "PhongBan";
    private static final String SHEET_NHANSU = "NhanSu";
    // Định dạng ngày tháng để lưu vào Excel cho nhất quán
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Khởi tạo file Excel. Nếu file chưa có, sẽ tạo mới với các cột tiêu đề.
     */
    public static void initializeDatabase() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                // 1. Tạo sheet (trang tính) cho Phòng Ban
                Sheet phongBanSheet = workbook.createSheet(SHEET_PHONGBAN);
                Row pbHeader = phongBanSheet.createRow(0);
                String[] pbColumns = {"maPhong", "tenPhong", "maTruongPhong", "sdtPhong", "emailPhong", "tongSoNhanVien"};
                for (int i = 0; i < pbColumns.length; i++) {
                    pbHeader.createCell(i).setCellValue(pbColumns[i]);
                }
                // Thêm phòng "P00 - Chờ phân công" làm mặc định
                Row defaultPbRow = phongBanSheet.createRow(1);
                defaultPbRow.createCell(0).setCellValue("P00");
                defaultPbRow.createCell(1).setCellValue("Chờ phân công");
                defaultPbRow.createCell(5).setCellValue(0);

                // 2. Tạo sheet (trang tính) cho Nhân Sự
                Sheet nhanSuSheet = workbook.createSheet(SHEET_NHANSU);
                Row nsHeader = nhanSuSheet.createRow(0);
                String[] nsColumns = {"maNV", "hoTen", "gioiTinh", "ngaySinh", "cccd", "email", "sdt", "maPhongBan", "chucVu"};
                for (int i = 0; i < nsColumns.length; i++) {
                    nsHeader.createCell(i).setCellValue(nsColumns[i]);
                }

                // 3. Lưu file lại
                try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                    workbook.write(fileOut);
                }
            } catch (IOException e) {
                canhbao.canhbao("Lỗi", "Không thể tạo file Excel: " + e.getMessage());
            }
        }
    }

    /**
     * Đọc toàn bộ danh sách phòng ban từ file Excel.
     */
    public static List<PhongBan> loadAllPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(SHEET_PHONGBAN);
            if (sheet == null) return list;

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next(); // Bỏ qua dòng tiêu đề

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String maPhong = getStringCellValue(row.getCell(0));
                if (maPhong == null || maPhong.isEmpty()) continue;

                list.add(new PhongBan(
                        maPhong,
                        getStringCellValue(row.getCell(1)),
                        getStringCellValue(row.getCell(2)),
                        getStringCellValue(row.getCell(3)),
                        getStringCellValue(row.getCell(4)),
                        (int) getNumericCellValue(row.getCell(5))
                ));
            }
        } catch (IOException e) {
            canhbao.canhbao("Lỗi", "Không thể đọc dữ liệu Phòng Ban từ Excel: " + e.getMessage());
        }
        return list;
    }

    /**
     * Đọc toàn bộ danh sách nhân sự từ file Excel.
     */
    public static List<NhanSu> loadAllNhanSu() {
        List<NhanSu> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(SHEET_NHANSU);
            if (sheet == null) return list;

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next(); // Bỏ qua dòng tiêu đề

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String maNV = getStringCellValue(row.getCell(0));
                if (maNV == null || maNV.isEmpty()) continue;

                String ngaySinhStr = getStringCellValue(row.getCell(3));
                LocalDate ngaySinh = (ngaySinhStr != null && !ngaySinhStr.isEmpty()) ? LocalDate.parse(ngaySinhStr, DATE_FORMATTER) : null;

                list.add(new NhanSu(
                        maNV,
                        getStringCellValue(row.getCell(1)),
                        getStringCellValue(row.getCell(2)),
                        ngaySinh,
                        getStringCellValue(row.getCell(4)),
                        getStringCellValue(row.getCell(5)),
                        getStringCellValue(row.getCell(6)),
                        getStringCellValue(row.getCell(7)),
                        getStringCellValue(row.getCell(8))
                ));
            }
        } catch (IOException e) {
            canhbao.canhbao("Lỗi", "Không thể đọc dữ liệu Nhân Sự từ Excel: " + e.getMessage());
        }
        return list;
    }

    /**
     * Ghi toàn bộ danh sách phòng ban vào Excel (ghi đè lên dữ liệu cũ).
     */
    public static void writeAllPhongBan(List<PhongBan> list) {
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(SHEET_PHONGBAN);

            // Xóa hết các dòng dữ liệu cũ để chuẩn bị ghi mới
            for (int i = sheet.getLastRowNum(); i > 0; i--) {
                sheet.removeRow(sheet.getRow(i));
            }

            // Bắt đầu ghi từ dòng thứ 2 (dòng 1 là tiêu đề)
            int rowNum = 1;
            for (PhongBan pb : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pb.getMaPhong());
                row.createCell(1).setCellValue(pb.getTenPhong());
                row.createCell(2).setCellValue(pb.getMaTruongPhong());
                row.createCell(3).setCellValue(pb.getSdtPhong());
                row.createCell(4).setCellValue(pb.getEmailPhong());
                row.createCell(5).setCellValue(pb.getTongSoNhanVien());
            }

            // Lưu những thay đổi vào file
            try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            canhbao.canhbao("Lỗi", "Không thể ghi dữ liệu Phòng Ban vào Excel: " + e.getMessage());
        }
    }

    /**
     * Ghi toàn bộ danh sách nhân sự vào Excel (ghi đè lên dữ liệu cũ).
     */
    public static void writeAllNhanSu(List<NhanSu> list) {
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(SHEET_NHANSU);

            for (int i = sheet.getLastRowNum(); i > 0; i--) {
                sheet.removeRow(sheet.getRow(i));
            }
            
            int rowNum = 1;
            for (NhanSu ns : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ns.getMaNV());
                row.createCell(1).setCellValue(ns.getHoTen());
                row.createCell(2).setCellValue(ns.getGioiTinh());
                if (ns.getNgaySinh() != null) {
                   row.createCell(3).setCellValue(ns.getNgaySinh().format(DATE_FORMATTER));
                }
                row.createCell(4).setCellValue(ns.getCccd());
                row.createCell(5).setCellValue(ns.getEmail());
                row.createCell(6).setCellValue(ns.getSdt());
                row.createCell(7).setCellValue(ns.getMaPhongBan());
                row.createCell(8).setCellValue(ns.getChucVu());
            }

            try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            canhbao.canhbao("Lỗi", "Không thể ghi dữ liệu Nhân Sự vào Excel: " + e.getMessage());
        }
    }

    // --- Các hàm hỗ trợ đọc dữ liệu từ các ô trong Excel một cách an toàn ---
    private static String getStringCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return new DataFormatter().formatCellValue(cell);
            default:
                return null;
        }
    }

    private static double getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return 0;
        return cell.getNumericCellValue();
    }
}
