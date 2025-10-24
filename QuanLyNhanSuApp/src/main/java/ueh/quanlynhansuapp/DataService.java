/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataService {
    private static DataService instance;
    private final DatabaseManager dbManager; // Chỉ cần một đối tượng quản lý DB

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList();

    private DataService() {
        dbManager = new DatabaseManager();
        reloadAllData(); // Tải dữ liệu lần đầu khi khởi động
    }

    // Tải lại toàn bộ dữ liệu từ CSDL và cập nhật lên giao diện
    public final void reloadAllData() {
        dsPhongBan.setAll(dbManager.loadAllPhongBan());
        dsNhanSu.setAll(dbManager.loadAllNhanSu());
    }

    // Các hàm get và tìm kiếm (không thay đổi)
    public ObservableList<NhanSu> getDsNhanSu() { return dsNhanSu; }
    public ObservableList<PhongBan> getDsPhongBan() { return dsPhongBan; }

    public PhongBan timPhongBanTheoMa(String maPhong) {
        return dsPhongBan.stream()
                .filter(pb -> pb.getMaPhong().equalsIgnoreCase(maPhong))
                .findFirst()
                .orElse(null);
    }

    public NhanSu timNhanSuTheoMa(String maNV) {
        return dsNhanSu.stream()
                .filter(ns -> ns.getMaNV().equalsIgnoreCase(ns.getMaNV()))
                .findFirst()
                .orElse(null);
    }

    // --- Các hàm Thêm, Sửa, Xóa ---
    // Logic: Gọi DatabaseManager để thay đổi CSDL, sau đó tải lại toàn bộ dữ liệu

    public boolean addPhongBan(PhongBan pb) {
        if (dbManager.addPhongBan(pb)) {
            reloadAllData();
            return true;
        }
        return false;
    }

    public void deletePhongBan(PhongBan selectedPhongBan) {
        // Gọi hàm transaction đã được tối ưu trong DatabaseManager
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
}