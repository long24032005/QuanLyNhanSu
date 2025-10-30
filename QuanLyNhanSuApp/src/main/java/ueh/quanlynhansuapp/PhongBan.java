/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

/*
Lớp PhongBan mô tả thông tin của một phòng ban trong công ty.
Mỗi đối tượng PhongBan tương ứng với một dòng trong bảng "phongban" trong cơ sở dữ liệu.
*/
public class PhongBan {

    private String maPhong;
    private String tenPhong;
    private String maTruongPhong;
    private String sdtPhong;
    private String emailPhong;
    private int tongSoNhanVien;

    // Constructor mặc định (dùng khi cần tạo đối tượng trống)
    public PhongBan() {
    }

    // Constructor đầy đủ tham số (dùng khi đọc dữ liệu từ db hoặc thêm mới)
    public PhongBan(String maPhong, String tenPhong, String maTruongPhong, String sdtPhong, String emailPhong, int tongSoNhanVien) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.maTruongPhong = maTruongPhong;
        this.sdtPhong = sdtPhong;
        this.emailPhong = emailPhong;
        this.tongSoNhanVien = tongSoNhanVien;
    }

    // getter/setter
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

    // Hàm hỗ trợ hiển thị debug
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