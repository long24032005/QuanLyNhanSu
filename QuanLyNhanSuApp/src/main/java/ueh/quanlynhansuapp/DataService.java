/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataService {
    private static DataService instance;
    private final DatabaseManager dbManager; // Quản lý truy cập CSDL

    // === Danh sách dữ liệu trong bộ nhớ ===
    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList();
    private final ObservableList<LuongThuong> dsLuongThuong = FXCollections.observableArrayList();

    // === Singleton ===
    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    private DataService() {
        dbManager = new DatabaseManager();
        reloadAllData(); // Tải dữ liệu lần đầu khi khởi động
    }

    // ==========================================================
    // 🔹 TẢI LẠI DỮ LIỆU
    // ==========================================================
    public final void reloadAllData() {
        dsPhongBan.setAll(dbManager.loadAllPhongBan());
        dsNhanSu.setAll(dbManager.loadAllNhanSu());
        dsLuongThuong.setAll(dbManager.loadAllLuongThuong());
        System.out.println("✅ Dữ liệu đã tải: " + dsNhanSu.size() + " nhân sự, " +
                dsPhongBan.size() + " phòng ban, " + dsLuongThuong.size() + " bản ghi lương thưởng.");
    }

    // ==========================================================
    // 🔹 GETTER CƠ BẢN
    // ==========================================================
    public ObservableList<NhanSu> getDsNhanSu() { return dsNhanSu; }
    public ObservableList<PhongBan> getDsPhongBan() { return dsPhongBan; }
    public ObservableList<LuongThuong> getDsLuongThuong() { return dsLuongThuong; }

    // ==========================================================
    // 🔹 TRA CỨU CƠ BẢN
    // ==========================================================
    public PhongBan timPhongBanTheoMa(String maPhong) {
        return dsPhongBan.stream()
                .filter(pb -> pb.getMaPhong().equalsIgnoreCase(maPhong))
                .findFirst()
                .orElse(null);
    }

    public NhanSu timNhanSuTheoMa(String maNV) {
        if (maNV == null || maNV.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getMaNV().equalsIgnoreCase(maNV)) {
                return ns;
            }
        }
        return null;
    }
    
    // ======= KIỂM TRA TRÙNG CCCD =======
    public NhanSu timNhanSuTheoCCCD(String cccd) {
        if (cccd == null || cccd.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getCccd() != null && ns.getCccd().equals(cccd)) {
                return ns; // trả về nhân viên bị trùng
            }
        }
        return null; // không trùng
    }

    // ======= KIỂM TRA TRÙNG EMAIL =======
    public NhanSu timNhanSuTheoEmail(String email) {
        if (email == null || email.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getEmail() != null && ns.getEmail().equalsIgnoreCase(email)) {
                return ns;
            }
        }
        return null;
    }

    // ======= KIỂM TRA TRÙNG SỐ ĐIỆN THOẠI =======
    public NhanSu timNhanSuTheoSDT(String sdt) {
        if (sdt == null || sdt.isEmpty()) return null;
        for (NhanSu ns : dsNhanSu) {
            if (ns.getSdt() != null && ns.getSdt().equals(sdt)) {
                return ns;
            }
        }
        return null;
    }

    // ==========================================================
    // 🔹 LẤY DANH SÁCH LƯƠNG THEO MÃ NHÂN VIÊN (HÀM QUAN TRỌNG)
    // ==========================================================
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

    // ==========================================================
    // 🔹 CÁC HÀM THÊM / XÓA / SỬA
    // ==========================================================
    public boolean addPhongBan(PhongBan pb) {
        if (dbManager.addPhongBan(pb)) {
            reloadAllData();
            return true;
        }
        return false;
    }

    public void deletePhongBan(PhongBan selectedPhongBan) {
        if (dbManager.deletePhongBanAndReassignNhanSu(selectedPhongBan.getMaPhong())) {
            reloadAllData();
        }
    }

    public void updatePhongBan(PhongBan pbDaSua) {
        if (dbManager.updatePhongBan(pbDaSua)) {
            reloadAllData();
        }
    }

    public boolean addNhanSu(NhanSu ns) {
        if (dbManager.addNhanSu(ns)) {
            reloadAllData();
            return true;
        }
        return false;
    }

    public void deleteNhanSu(NhanSu ns) {
        if (dbManager.deleteNhanSu(ns.getMaNV())) {
            reloadAllData();
        }
    }

    public void updateNhanSu(NhanSu nsDaSua) {
        if (dbManager.updateNhanSu(nsDaSua)) {
            reloadAllData();
        }
    }

    public boolean addLuongThuong(LuongThuong lt) {
        if (dbManager.addLuongThuong(lt)) {
            reloadAllData();
            return true;
        }
        return false;
    }

    public void updateLuongThuong(LuongThuong lt) {
        if (dbManager.updateLuongThuong(lt)) {
            reloadAllData();
        }
    }

    public void deleteLuongThuong(LuongThuong lt) {
        if (dbManager.deleteLuongThuong(lt.getMaLuong())) {
            reloadAllData();
        }
    }
}
