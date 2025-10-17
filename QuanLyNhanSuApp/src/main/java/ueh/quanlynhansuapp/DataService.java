/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lagia
 */
public class DataService {
    // --- Singleton Pattern: Đảm bảo chỉ có một instance duy nhất của DataService ---
    private static DataService instance;
    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    // --- Các danh sách Observable để UI tự động cập nhật ---
    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList();

    /**
     * Constructor private để ngăn việc tạo instance từ bên ngoài.
     * Khi được gọi lần đầu, nó sẽ khởi tạo và tải toàn bộ dữ liệu từ database.
     */
    private DataService() {
        Database.initializeDatabase(); // Tạo bảng nếu chưa tồn tại
        dsPhongBan.setAll(Database.loadAllPhongBan());
        dsNhanSu.setAll(Database.loadAllNhanSu());
    }

    // --- Cung cấp quyền truy cập (chỉ đọc) vào danh sách cho các Controller ---
    public ObservableList<NhanSu> getDsNhanSu() { return dsNhanSu; }
    public ObservableList<PhongBan> getDsPhongBan() { return dsPhongBan; }

    // ================== LOGIC NGHIỆP VỤ VÀ TƯƠNG TÁC DATABASE ==================

    public PhongBan timPhongBanTheoMa(String maPhong) {
        return dsPhongBan.stream()
                         .filter(pb -> pb.getMaPhong().equalsIgnoreCase(maPhong))
                         .findFirst()
                         .orElse(null);
    }

    public NhanSu timNhanSuTheoMa(String maNV) {
        return dsNhanSu.stream()
                       .filter(ns -> ns.getMaNV().equalsIgnoreCase(maNV))
                       .findFirst()
                       .orElse(null);
    }

    public boolean addPhongBan(PhongBan pb) {
        if (Database.insertPhongBan(pb)) {
            dsPhongBan.add(pb); // Cập nhật danh sách trên UI
            return true;
        }
        return false;
    }

    public void deletePhongBan(PhongBan selectedPhongBan) {
        String maPhongCanXoa = selectedPhongBan.getMaPhong();

        // Cập nhật tất cả nhân viên thuộc phòng bị xóa sang phòng "Chờ phân công"
        dsNhanSu.forEach(ns -> {
            if (ns.getMaPhongBan() != null && ns.getMaPhongBan().equals(maPhongCanXoa)) {
                ns.setMaPhongBan("P00"); // Mã phòng mặc định
                Database.updateNhanSu(ns); // Cập nhật thay đổi trong database
            }
        });

        // Tăng số lượng nhân viên cho phòng "P00"
        PhongBan phongCho = timPhongBanTheoMa("P00");
        if (phongCho != null) {
            long soNhanVienChuyenDen = dsNhanSu.stream().filter(ns -> ns.getMaPhongBan().equals("P00")).count();
            // Cần tính lại chính xác hơn, ở đây ta chỉ cần update lại sau khi xóa
            // Logic tính toán lại số lượng nhân viên cho mỗi phòng nên được thực hiện lại
        }
        
        // Xóa phòng ban khỏi database
        if (Database.deletePhongBan(maPhongCanXoa)) {
            dsPhongBan.remove(selectedPhongBan); // Cập nhật danh sách trên UI
            // Cần refresh lại bảng nhân sự để hiển thị mã phòng ban mới
        }
    }

    public boolean addNhanSu(NhanSu ns) {
        if (Database.insertNhanSu(ns)) {
            dsNhanSu.add(ns); // Cập nhật UI
            // Tăng tổng số nhân viên của phòng ban tương ứng
            PhongBan pb = timPhongBanTheoMa(ns.getMaPhongBan());
            if (pb != null) {
                pb.setTongSoNhanVien(pb.getTongSoNhanVien() + 1);
                Database.updatePhongBan(pb); // Cập nhật lại trong DB
            }
            return true;
        }
        return false;
    }

    public void deleteNhanSu(NhanSu ns) {
        if (Database.deleteNhanSu(ns.getMaNV())) {
            dsNhanSu.remove(ns); // Cập nhật UI
            // Giảm số lượng nhân viên của phòng ban cũ
            PhongBan pb = timPhongBanTheoMa(ns.getMaPhongBan());
            if (pb != null) {
                pb.setTongSoNhanVien(pb.getTongSoNhanVien() - 1);
                Database.updatePhongBan(pb);
            }
        }
    }
    public void updateNhanSu(NhanSu nsDaSua) {
    Database.updateNhanSu(nsDaSua); // 1. Cập nhật vào DB

    // 2. Tìm và cập nhật đối tượng trong danh sách ObservableList
    // Điều này sẽ tự động kích hoạt cập nhật trên TableView
    for (int i = 0; i < dsNhanSu.size(); i++) {
        if (dsNhanSu.get(i).getMaNV().equals(nsDaSua.getMaNV())) {
            dsNhanSu.set(i, nsDaSua);
            break;
        }
    }
}

    public void updatePhongBan(PhongBan pbDaSua) {
        Database.updatePhongBan(pbDaSua); // 1. Cập nhật vào DB
    
        // 2. Cập nhật đối tượng trong danh sách
        for (int i = 0; i < dsPhongBan.size(); i++) {
            if (dsPhongBan.get(i).getMaPhong().equals(pbDaSua.getMaPhong())) {
                dsPhongBan.set(i, pbDaSua);
                break;
           }
        }
    }
}
