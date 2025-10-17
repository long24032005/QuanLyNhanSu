/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author lagia
 */
public class DataService {
    private static DataService instance;
    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    private final ObservableList<NhanSu> dsNhanSu = FXCollections.observableArrayList();
    private final ObservableList<PhongBan> dsPhongBan = FXCollections.observableArrayList();

    private DataService() {
        // Thay vì khởi tạo bảng SQL, giờ ta khởi tạo file Excel
        Database.initializeDatabase(); 
        // Tải dữ liệu từ Excel lên khi chương trình bắt đầu
        dsPhongBan.setAll(Database.loadAllPhongBan());
        dsNhanSu.setAll(Database.loadAllNhanSu());
        
        // Sau khi tải xong, tính lại tổng số nhân viên cho chắc chắn
        recalculateAndUpdateTongNhanVien();
    }

    // Các hàm get và tìm kiếm không thay đổi
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
                       .filter(ns -> ns.getMaNV().equalsIgnoreCase(maNV))
                       .findFirst()
                       .orElse(null);
    }

    // --- Các hàm thêm, xóa, sửa sẽ gọi đến Database để ghi lại file Excel ---

    public boolean addPhongBan(PhongBan pb) {
        dsPhongBan.add(pb);
        // Sau khi thêm, ghi lại toàn bộ danh sách vào Excel
        Database.writeAllPhongBan(dsPhongBan); 
        return true;
    }

    public void deletePhongBan(PhongBan selectedPhongBan) {
        String maPhongCanXoa = selectedPhongBan.getMaPhong();
        dsNhanSu.forEach(ns -> {
            if (ns.getMaPhongBan() != null && ns.getMaPhongBan().equals(maPhongCanXoa)) {
                ns.setMaPhongBan("P00");
            }
        });

        dsPhongBan.remove(selectedPhongBan);
        
        // Tính lại số nhân viên và ghi lại cả 2 file
        recalculateAndUpdateTongNhanVien(); 
        Database.writeAllNhanSu(dsNhanSu);
    }

    public boolean addNhanSu(NhanSu ns) {
        dsNhanSu.add(ns);
        recalculateAndUpdateTongNhanVien();
        Database.writeAllNhanSu(dsNhanSu);
        return true;
    }

    public void deleteNhanSu(NhanSu ns) {
        dsNhanSu.remove(ns);
        recalculateAndUpdateTongNhanVien();
        Database.writeAllNhanSu(dsNhanSu);
    }

    public void updateNhanSu(NhanSu nsDaSua, String maPhongBanCu) {
        for (int i = 0; i < dsNhanSu.size(); i++) {
            if (dsNhanSu.get(i).getMaNV().equals(nsDaSua.getMaNV())) {
                dsNhanSu.set(i, nsDaSua);
                break;
            }
        }
        if (!maPhongBanCu.equals(nsDaSua.getMaPhongBan())) {
            recalculateAndUpdateTongNhanVien();
        }
        Database.writeAllNhanSu(dsNhanSu);
    }
    
    public void updatePhongBan(PhongBan pbDaSua) {
        for (int i = 0; i < dsPhongBan.size(); i++) {
            if (dsPhongBan.get(i).getMaPhong().equals(pbDaSua.getMaPhong())) {
                dsPhongBan.set(i, pbDaSua);
                break;
            }
        }
        Database.writeAllPhongBan(dsPhongBan);
    }

    // Hàm này rất quan trọng: nó đếm lại số nhân viên mỗi phòng rồi ghi vào Excel
    private void recalculateAndUpdateTongNhanVien() {
        Map<String, Long> counts = dsNhanSu.stream()
                .collect(Collectors.groupingBy(NhanSu::getMaPhongBan, Collectors.counting()));

        for (PhongBan pb : dsPhongBan) {
            long count = counts.getOrDefault(pb.getMaPhong(), 0L);
            pb.setTongSoNhanVien((int) count);
        }

        Database.writeAllPhongBan(dsPhongBan);
    }
}
