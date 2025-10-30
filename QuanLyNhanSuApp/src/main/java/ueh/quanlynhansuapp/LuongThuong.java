package ueh.quanlynhansuapp;

/*
 - Lớp LuongThuong khai báo thông tin một bản ghi lương thưởng của nhân viên.
 - Mỗi đối tượng tương ứng với một dòng trong bảng "luongthuong" của database.
 */
public class LuongThuong {
    private String maLuong;
    private String maNhanVien;
    private String thangNam;     // "MM/yyyy"
    private double luongCoBan;
    private double phuCap;
    private double thuong;
    private double khauTru;
    private String ngayChiTra;   // "yyyy-MM-dd"

    /*
     - Hàm khởi tạo nhận đầy đủ thông tin một bản lương. 
     - Dùng phương thức nn() để tránh lỗi null (chuyển null thành chuỗi rỗng).
    */
    public LuongThuong(String maLuong,  
                       String maNhanVien, 
                       String thangNam,                       
                       double luongCoBan,
                       double phuCap,
                       double thuong,
                       double khauTru,
                       String ngayChiTra) { // Ngày chi trả lương, lưu dưới dạng chuỗi yyyy-MM-dd
        this.maLuong = nn(maLuong);
        this.maNhanVien = nn(maNhanVien);
        this.thangNam = nn(thangNam);
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.thuong = thuong;
        this.khauTru = khauTru;
        this.ngayChiTra = nn(ngayChiTra);
    }

    //  Nếu chuỗi bị null thì trả về rỗng =>  tránh lỗi NullPointerException khi hiển thị hoặc lưu dữ liệu
    private String nn(String s) { return s == null ? "" : s; }

    // Getter/Setter cho từng  thuộc tính
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

    
    // Hàm tính toán và hỗ trợ
    
    // Tổng lương = lương cơ bản + phụ cấp + thưởng - khấu trừ 
    public double getTongLuong() {
        return luongCoBan + phuCap + thuong - khauTru;
    }

    /*
     - Setter giả cho thuộc tính tongLuong.
     - Lý do: khi dùng TableView trong JavaFX, nếu cột có cả getter và setter thì nó mới không báo lỗi “property not writeable”=> Hàm này không làm gì cả (no-op).
     */
    public void setTongLuong(double ignored) { /* no-op */ }

    // Trả về chuỗi mô tả ngắn gọn một bản lương để dễ xem trong log
    @Override
    public String toString() {
        return maLuong + " - " + maNhanVien + " - " + thangNam;
    }
}
