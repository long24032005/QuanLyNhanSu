/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.time.LocalDate;

/*
Lớp NhanSu mô tả thông tin của một nhân viên trong hệ thống.
Mỗi đối tượng tương ứng với một dòng trong bảng "nhansu" trong cơ sở dữ liệu.
*/
public class NhanSu {
    
    private String maNV;
    private String hoTen;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String cccd;
    private String email;
    private String sdt;
    private String maPhongBan;
    private String chucVu;
    private String matKhau;

    // Constructor mặc định (dùng khi cần tạo đối tượng rỗng)
    public NhanSu() {
    }

    // Constructor đầy đủ tham số (dùng khi đọc từ db hoặc thêm mới)
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

    // getter/setter 
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
    
   
    public String getMatKhau() {return this.matKhau; }

    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    // Hàm hỗ trợ dùng trong debug
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