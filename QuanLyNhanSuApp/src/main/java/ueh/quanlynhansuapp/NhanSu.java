/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.time.LocalDate;

public class NhanSu {

    // Các thuộc tính đã tuân thủ quy ước camelCase
    private String maNV;
    private String hoTen;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String cccd;
    private String email;
    private String sdt;
    private String maPhongBan;
    private String chucVu;

    // Constructor mặc định
    public NhanSu() {
    }

    // Constructor đầy đủ tham số
    public NhanSu(String maNV, String hoTen, String gioiTinh, LocalDate ngaySinh, String cccd, String email, String sdt, String maPhongBan, String chucVu) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.email = email;
        this.sdt = sdt;
        this.maPhongBan = maPhongBan;
        this.chucVu = chucVu;
    }

    // --- GETTERS AND SETTERS ---
    // Các phương thức đã tuân thủ quy ước
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getMaPhongBan() { return maPhongBan; }
    public void setMaPhongBan(String maPhongBan) { this.maPhongBan = maPhongBan; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }


    @Override
    public String toString() {
        return "NhanSu{" +
                "maNV='" + maNV + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", cccd='" + cccd + '\'' +
                ", email='" + email + '\'' +
                ", sdt='" + sdt + '\'' +
                ", maPhongBan='" + maPhongBan + '\'' +
                ", chucVu='" + chucVu + '\'' +
                '}';
    }
}