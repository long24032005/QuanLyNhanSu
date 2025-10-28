package ueh.quanlynhansuapp;

public class LuongThuong {
    private String maLuong;
    private String maNhanVien;
    private String thangNam;     // "MM/yyyy"
    private double luongCoBan;
    private double phuCap;
    private double thuong;
    private double khauTru;
    private String ngayChiTra;   // "yyyy-MM-dd"

    public LuongThuong(String maLuong,
                       String maNhanVien,
                       String thangNam,
                       double luongCoBan,
                       double phuCap,
                       double thuong,
                       double khauTru,
                       String ngayChiTra) {
        this.maLuong = nn(maLuong);
        this.maNhanVien = nn(maNhanVien);
        this.thangNam = nn(thangNam);
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.thuong = thuong;
        this.khauTru = khauTru;
        this.ngayChiTra = nn(ngayChiTra);
    }

    private String nn(String s) { return s == null ? "" : s; }

    // Getters/Setters
    public String getMaLuong() { return maLuong; }
    public void setMaLuong(String maLuong) { this.maLuong = nn(maLuong); }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = nn(maNhanVien); }

    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = nn(thangNam); }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public double getPhuCap() { return phuCap; }
    public void setPhuCap(double phuCap) { this.phuCap = phuCap; }

    public double getThuong() { return thuong; }
    public void setThuong(double thuong) { this.thuong = thuong; }

    public double getKhauTru() { return khauTru; }
    public void setKhauTru(double khauTru) { this.khauTru = khauTru; }

    public String getNgayChiTra() { return ngayChiTra; }
    public void setNgayChiTra(String ngayChiTra) { this.ngayChiTra = nn(ngayChiTra); }

    /** Tổng lương = lương cơ bản + phụ cấp + thưởng - khấu trừ */
    public double getTongLuong() {
        return luongCoBan + phuCap + thuong - khauTru;
    }

    /** Setter giả để tránh lỗi khi TableColumn đòi property writeable */
    public void setTongLuong(double ignored) { /* no-op */ }

    @Override
    public String toString() {
        return maLuong + " - " + maNhanVien + " - " + thangNam;
    }
}
