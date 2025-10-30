/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/*
Lớp DataService là tầng trung gian giữa Controller và Database.
 - Lưu trữ dữ liệu đã tải vào bộ nhớ.
 - Cung cấp các hàm truy vấn, thêm, xóa, sửa.
 */

public class DataService {
    private static DataService instance; // Đối tượng duy nhất của lớp này
    private final DatabaseManager dbManager; // Dùng để thao tác với database

    // Danh sách dữ liệu trong bộ nhớ
    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList();
    private final ObservableList<LuongThuong> dsLuongThuong = FXCollections.observableArrayList();

    // Lấy ra instance duy nhất của lớp (nếu chưa có thì tạo mới)
    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }
    // Hàm khởi tạo: kết nối với DatabaseManager và tải dữ liệu ban đầu
    private DataService() {
        dbManager = new DatabaseManager();
        reloadAllData(); // Tải dữ liệu lần đầu khi khởi động
    }

    // Tải lại toàn bộ dữ liệu từ database vào các danh sách 
    public final void reloadAllData() {
        dsPhongBan.setAll(dbManager.loadAllPhongBan());
        dsNhanSu.setAll(dbManager.loadAllNhanSu());
        dsLuongThuong.setAll(dbManager.loadAllLuongThuong());
        System.out.println("Dữ liệu đã tải: " + dsNhanSu.size() + " nhân sự, " +
                dsPhongBan.size() + " phòng ban, " + dsLuongThuong.size() + " bản ghi lương thưởng.");
    }

    // Trả về danh sách hiện tại
    public ObservableList<NhanSu> getDsNhanSu() { return dsNhanSu; }
    public ObservableList<PhongBan> getDsPhongBan() { return dsPhongBan; }
    public ObservableList<LuongThuong> getDsLuongThuong() { return dsLuongThuong; }

    
    // Các hàm tìm kiếm cơ bản
    
    //  Tìm phòng ban theo mã phòng ban
    public PhongBan timPhongBanTheoMa(String maPhong) {
        return dsPhongBan.stream()
                .filter(pb -> pb.getMaPhong().equalsIgnoreCase(maPhong))
                .findFirst()
                .orElse(null);
    }
    // Tìm nhân sự theo mã nhân viên
    public NhanSu timNhanSuTheoMa(String maNV) {
        if (maNV == null || maNV.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getMaNV().equalsIgnoreCase(maNV)) {
                return ns;
            }
        }
        return null;
    }
    
    // Kiểm tra trùng CCCD
    public NhanSu timNhanSuTheoCCCD(String cccd) {
        if (cccd == null || cccd.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getCccd() != null && ns.getCccd().equals(cccd)) {
                return ns; // trả về nhân viên bị trùng
            }
        }
        return null; // không trùng
    }

    // Kiểm tra trùng Email
    public NhanSu timNhanSuTheoEmail(String email) {
        if (email == null || email.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getEmail() != null && ns.getEmail().equalsIgnoreCase(email)) {
                return ns;
            }
        }
        return null;
    }

    // Kiểm tra trùng SDT
    public NhanSu timNhanSuTheoSDT(String sdt) {
        if (sdt == null || sdt.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getSdt() != null && ns.getSdt().equals(sdt)) {
                return ns;
            }
        }
        return null;
    }

    // Lấy danh sách lương thưởng theo mã nhân viên
    public ObservableList<LuongThuong> getLuongByMaNV(String maNV) {
        ObservableList<LuongThuong> ketQua = FXCollections.observableArrayList();
        if (maNV == null || maNV.trim().isEmpty()) {
            return ketQua;
        }

        String maNVCanTim = maNV.trim();

        for (LuongThuong lt : dsLuongThuong) {
            String maNVTrongLuong = lt.getMaNhanVien();
            if (maNVTrongLuong != null && maNVTrongLuong.trim().equalsIgnoreCase(maNVCanTim)) {
                ketQua.add(lt);
            }
        }

        System.out.println("--- [DEBUG-DataService] Tìm lương cho '" + maNVCanTim +
                "', kết quả: " + ketQua.size() + " bản ghi.");
        return ketQua;
    }

    
    // Các hàm thêm / xóa / sửa dữ liệu
    
    // Thêm phòng ban mới vào database
    public boolean addPhongBan(PhongBan pb) {
        if (dbManager.addPhongBan(pb)) {
            reloadAllData();
            return true;
        }
        return false;
    }
    // Xóa phòng ban và chuyển nhân viên của phòng đó về mã 'P00
    public void deletePhongBan(PhongBan phongBan) {
        boolean ok = dbManager.deletePhongBanAndReassignNhanSu(phongBan.getMaPhong());
        if (ok) {
            reloadAllData();
            canhbao.thongbao("Thành công", "Đã xóa phòng ban và di chuyển nhân viên sang 'P00'.");
        } else {
            canhbao.canhbao("Lỗi", "Không thể xóa phòng ban hoặc di chuyển nhân viên.");
        }
    }
    // Cập nhật lại thông tin phòng ban
    public void updatePhongBan(PhongBan pbDaSua) {
        if (dbManager.updatePhongBan(pbDaSua)) {
            reloadAllData();
        }
    }
    //  Thêm nhân sự mới
    public boolean addNhanSu(NhanSu ns) {
        if (dbManager.addNhanSu(ns)) {
            reloadAllData();
            return true;
        }
        return false;
    }
    // Xóa nhân sự theo mã NV
    public void deleteNhanSu(NhanSu ns) {
        if (dbManager.deleteNhanSu(ns.getMaNV())) {
            reloadAllData();
        }
    }
    // Cập nhật thông tin nhân sự
    public void updateNhanSu(NhanSu nsDaSua) {
        if (dbManager.updateNhanSu(nsDaSua)) {
            reloadAllData();
        }
    }
    // Thêm bản ghi lương thưởng
    public boolean addLuongThuong(LuongThuong lt) {
        if (dbManager.addLuongThuong(lt)) {
            reloadAllData();
            return true;
        }
        return false;
    }
    // Cập nhật bản ghi lương thưởng
    public void updateLuongThuong(LuongThuong lt) {
        if (dbManager.updateLuongThuong(lt)) {
            reloadAllData();
        }
    }
    // Xóa bản ghi lương thưởng
    public void deleteLuongThuong(LuongThuong lt) {
        if (dbManager.deleteLuongThuong(lt.getMaLuong())) {
            reloadAllData();
        }
    }
}
