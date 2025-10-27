/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */

public class LuongThuong {
    private String maLuong;
    private String maNhanVien;
    private String thangNam;     // ví dụ: "10/2025"
    private double luongCoBan;
    private double phuCap;       // <-- mới
    private double thuong;
    private double khauTru;
    private String ngayChiTra;   // <-- mới (ví dụ "25/10/2025")

    public LuongThuong(String maLuong,
                       String maNhanVien,
                       String thangNam,
                       double luongCoBan,
                       double phuCap,
                       double thuong,
                       double khauTru,
                       String ngayChiTra) {
        this.maLuong = nonNull(maLuong);
        this.maNhanVien = nonNull(maNhanVien);
        this.thangNam = nonNull(thangNam);
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.thuong = thuong;
        this.khauTru = khauTru;
        this.ngayChiTra = nonNull(ngayChiTra);
    }

    private String nonNull(String s) { return s == null ? "" : s; }

    // ===== Getter / Setter =====
    public String getMaLuong() { return maLuong; }
    public void setMaLuong(String maLuong) { this.maLuong = nonNull(maLuong); }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = nonNull(maNhanVien); }

    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = nonNull(thangNam); }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public double getPhuCap() { return phuCap; }            // <-- cần cho colphucap
    public void setPhuCap(double phuCap) { this.phuCap = phuCap; }

    public double getThuong() { return thuong; }
    public void setThuong(double thuong) { this.thuong = thuong; }

    public double getKhauTru() { return khauTru; }
    public void setKhauTru(double khauTru) { this.khauTru = khauTru; }

    public String getNgayChiTra() { return ngayChiTra; }    // <-- cần cho colngaychitra
    public void setNgayChiTra(String ngayChiTra) { this.ngayChiTra = nonNull(ngayChiTra); }

    /** TÍNH ĐỘNG tổng lương = lương cơ bản + phụ cấp + thưởng - khấu trừ */
    public double getTongLuong() {                           // <-- cần cho coltongluong
        return luongCoBan + phuCap + thuong - khauTru;
    }

    @Override
    public String toString() {
        return maLuong + " - " + maNhanVien + " - " + thangNam;
    }
}


