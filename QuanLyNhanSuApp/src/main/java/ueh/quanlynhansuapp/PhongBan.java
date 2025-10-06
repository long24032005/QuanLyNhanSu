/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/**
 *
 * @author philo
 */
public class PhongBan {

    // Các thuộc tính đã tuân thủ quy ước camelCase
    private String maPhong;
    private String tenPhong;
    private String maTruongPhong;
    private String sdtPhong;
    private String emailPhong;
    private int tongSoNhanVien;

    // Constructor mặc định
    public PhongBan() {
    }

    // Constructor đầy đủ tham số
    public PhongBan(String maPhong, String tenPhong, String maTruongPhong, String sdtPhong, String emailPhong, int tongSoNhanVien) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.maTruongPhong = maTruongPhong;
        this.sdtPhong = sdtPhong;
        this.emailPhong = emailPhong;
        this.tongSoNhanVien = tongSoNhanVien;
    }

    // --- GETTERS AND SETTERS ---
    // Các phương thức đã tuân thủ quy ước
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getMaTruongPhong() { return maTruongPhong; }
    public void setMaTruongPhong(String maTruongPhong) { this.maTruongPhong = maTruongPhong; }

    public String getSdtPhong() { return sdtPhong; }
    public void setSdtPhong(String sdtPhong) { this.sdtPhong = sdtPhong; }

    public String getEmailPhong() { return emailPhong; }
    public void setEmailPhong(String emailPhong) { this.emailPhong = emailPhong; }

    public int getTongSoNhanVien() { return tongSoNhanVien; }
    public void setTongSoNhanVien(int tongSoNhanVien) { this.tongSoNhanVien = tongSoNhanVien; }

    @Override
    public String toString() {
        return "PhongBan{" +
                "maPhong='" + maPhong + '\'' +
                ", tenPhong='" + tenPhong + '\'' +
                ", maTruongPhong='" + maTruongPhong + '\'' +
                ", sdtPhong='" + sdtPhong + '\'' +
                ", emailPhong='" + emailPhong + '\'' +
                ", tongSoNhanVien=" + tongSoNhanVien +
                '}';
    }
}