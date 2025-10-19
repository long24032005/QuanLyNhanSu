/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;

/**
 *
 * @author lagia
 */
public class NhanSuController {
    // --- KHAI BÁO BIẾN CHO NHÂN SỰ ---
    @FXML private TextField nhansu_txma;
    @FXML private TextField nhansu_txten;
    @FXML private ComboBox<String> nhansu_cbogioitinh;
    @FXML private DatePicker nhansu_datengaysinh;
    @FXML private TextField nhansu_txcccd;
    @FXML private TextField nhansu_txemail;
    @FXML private TextField nhansu_txsdt;
    @FXML private ComboBox<PhongBan> nhansu_cbmaPB;
    @FXML private ComboBox<String> nhansu_cbchucvu;

    @FXML private TableView<NhanSu> nhansu_tbnhansu;
    @FXML private TableColumn<NhanSu, String> nhansu_colma;
    @FXML private TableColumn<NhanSu, String> nhansu_colten;
    @FXML private TableColumn<NhanSu, String> nhansu_colgioitinh;
    @FXML private TableColumn<NhanSu, LocalDate> nhansu_colngaysinh;
    @FXML private TableColumn<NhanSu, String> nhansu_colcccd;
    @FXML private TableColumn<NhanSu, String> nhansu_colemail;
    @FXML private TableColumn<NhanSu, String> nhansu_colsdt;
    @FXML private TableColumn<NhanSu, String> nhansu_colmaPB;
    @FXML private TableColumn<NhanSu, String> nhansu_colchucvu;

    @FXML private Button nhansu_btthem;
    @FXML private Button nhansu_btxoa;
    @FXML private Button nhansu_btsua;
    @FXML private Button nhansu_bttimkiem;
    @FXML private Button nhansu_btquaylai;
    // Lấy instance của DataService để truy cập dữ liệu chung
    private final DataService dataService = DataService.getInstance();
    
    // Danh sách riêng cho ComboBox mã phòng ban
    private final ObservableList<String> dsMaPhongForCombo = FXCollections.observableArrayList();
    
    // Map để lưu Tên Phòng Ban -> Danh sách Chức Vụ
    private final Map<String, List<String>> phongBanToChucVuMap = new HashMap<>();
    // Map để lưu Chức Vụ -> Tên Phòng Ban
    private final Map<String, String> chucVuToPhongBanMap = new HashMap<>();
    // Danh sách chứa TẤT CẢ các chức vụ
    private final ObservableList<String> allChucVuList = FXCollections.observableArrayList();
    
    // ngăn các listener chạy khi không cần thiết
    private boolean dangCapNhatTuDong = false;

    @FXML
    public void initialize() {
        // --- CÀI ĐẶT CÁC CỘT CHO BẢNG NHÂN SỰ ---
        nhansu_colma.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        nhansu_colten.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        nhansu_colgioitinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        nhansu_colngaysinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        nhansu_colcccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        nhansu_colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        nhansu_colsdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        nhansu_colmaPB.setCellValueFactory(new PropertyValueFactory<>("maPhongBan"));
        nhansu_colchucvu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        // --- GÁN NGUỒN DỮ LIỆU TỪ DATASERVICE ---
        nhansu_tbnhansu.setItems(dataService.getDsNhanSu());

        // --- CÀI ĐẶT GIÁ TRỊ CHO CÁC COMBOBOX ---
        nhansu_cbogioitinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        
        // 1. Khởi tạo dữ liệu chức vụ
        initializeChucVuData();
        
        // 2. Mặc định, ComboBox chức vụ hiển thị TẤT CẢ chức vụ
        nhansu_cbchucvu.setItems(allChucVuList);
        
        // Tải danh sách mã phòng ban vào ComboBox lần đầu
        nhansu_cbmaPB.setItems(dataService.getDsPhongBan());
        nhansu_cbmaPB.setCellFactory(param -> new ListCell<PhongBan>() {
            @Override
            protected void updateItem(PhongBan pb, boolean empty) {
                super.updateItem(pb, empty);
                if (empty || pb == null) {
                    setText(null);
                } else {
                    setText(pb.getMaPhong() + " - " + pb.getTenPhong()); 
                }
            }
        });
        nhansu_cbmaPB.setButtonCell(new ListCell<PhongBan>() {
            @Override
            protected void updateItem(PhongBan pb, boolean empty) {
                super.updateItem(pb, empty);
                if (empty || pb == null) {
                    setText(null);
                } else {
                    // Định dạng này phải giống hệt ở trên
                    setText(pb.getMaPhong() + " - " + pb.getTenPhong()); 
                }
            }
        });
        
        // --- KHI CHỌN MỘT DÒNG TRONG BẢNG ---
        nhansu_tbnhansu.getSelectionModel().selectedItemProperty().addListener((obs, oldV, ns) -> {
            if (ns != null) {
                dangCapNhatTuDong = true;
                
                nhansu_txma.setText(ns.getMaNV());
                nhansu_txten.setText(ns.getHoTen());
                nhansu_cbogioitinh.setValue(ns.getGioiTinh());
                nhansu_datengaysinh.setValue(ns.getNgaySinh());
                nhansu_txcccd.setText(ns.getCccd());
                nhansu_txemail.setText(ns.getEmail());
                nhansu_txsdt.setText(ns.getSdt());
                // Đặt giá trị phòng ban TRƯỚC
                nhansu_cbmaPB.setValue(dataService.timPhongBanTheoMa(ns.getMaPhongBan()));
                // tự động lọc danh sách chức vụ sau đó đặt giá trị chức vụ
                nhansu_cbchucvu.setValue(ns.getChucVu());
                
                dangCapNhatTuDong = false;
            }
        });
        nhansu_cbmaPB.valueProperty().addListener((obs, oldMaPhong, newMaPhong) -> {
            String currentChucVu = nhansu_cbchucvu.getValue();
           
            if (newMaPhong == null) {
                // Nếu không chọn phòng ban, hiển thị TẤT CẢ chức vụ
                nhansu_cbchucvu.setItems(allChucVuList);
            } else {
                // Lấy TÊN phòng ban từ MÃ phòng ban
                PhongBan pb = newMaPhong;
                if (pb != null) {
                    String tenPhong = pb.getTenPhong();
                    // Lấy danh sách chức vụ tương ứng
                    List<String> chucVuList = phongBanToChucVuMap.get(tenPhong);
                    
                    if (chucVuList != null) {
                        nhansu_cbchucvu.setItems(FXCollections.observableArrayList(chucVuList));
                    } else {
                        // Nếu phòng ban này chưa được định nghĩa trong map (ví dụ phòng mới tạo)
                        // Tạm thời hiển thị danh sách rỗng
                        nhansu_cbchucvu.setItems(FXCollections.observableArrayList(" (Chưa có chức vụ) "));
                    }
                }
            }
            if (!dangCapNhatTuDong) {
                // Xóa lựa chọn chức vụ cũ để người dùng chọn lại
                nhansu_cbchucvu.getSelectionModel().clearSelection();
            } else {
                // Nếu là đang cập nhật tự động (tức là listener này được gọi do CHỨC VỤ thay đổi)
                // Ta phải set lại giá trị chức vụ (vì danh sách (items) của nó vừa bị thay đổi)
                nhansu_cbchucvu.setValue(currentChucVu);
            }
        });
        // cho ComboBox Chức Vụ (Scenario 2)
        nhansu_cbchucvu.valueProperty().addListener((obs, oldChucVu, newChucVu) -> {
            if (dangCapNhatTuDong) return; // Nếu đang set tự động thì bỏ qua

            // Chỉ tự động cập nhật phòng ban NẾU phòng ban đang trống
           PhongBan currentPhongBan = nhansu_cbmaPB.getValue();
            if (newChucVu != null && !newChucVu.isEmpty() && !newChucVu.contains("Chưa có chức vụ")) {
                
                // Tìm TÊN phòng từ chức vụ
                String tenPhongCanTim = chucVuToPhongBanMap.get(newChucVu);
                
                if (tenPhongCanTim != null) {
                    // Tìm MÃ phòng từ TÊN phòng
                    String maPhongCanSet = null;
                    for (PhongBan pb : dataService.getDsPhongBan()) {
                        if (pb.getTenPhong().equals(tenPhongCanTim)) {
                            maPhongCanSet = pb.getMaPhong();
                            break;
                        }
                    }
                    
                    if (maPhongCanSet != null) {
                        //để listener của cbmaPB không xóa lựa chọn chức vụ
                        dangCapNhatTuDong = true; 
                        nhansu_cbmaPB.setValue(dataService.timPhongBanTheoMa(maPhongCanSet));
                        dangCapNhatTuDong = false;
                    }
                }
            }
        });
    }
    /**
     * Khởi tạo dữ liệu mẫu cho các Map
     * Quan trọng: Tên phòng ban (key của Map) phải khớp 100%
     * với Tên phòng ban (tenPhong) được lưu trong file Excel.
     */
    private void initializeChucVuData() {
        // Dùng TÊN PHÒNG BAN làm key
        phongBanToChucVuMap.put("Phòng Kế toán", Arrays.asList("Trưởng phòng Kế toán", "Phó phòng Kế toán", "Kế toán trưởng", "Kế toán tổng hợp", "Kế toán viên", "Kế toán kho", "Kế toán thuế", "Thực tập sinh Kế toán"));
        phongBanToChucVuMap.put("Phòng Nhân sự", Arrays.asList("Trưởng phòng Nhân sự", "Phó phòng Nhân sự", "Chuyên viên Tuyển dụng", "Chuyên viên C&B", "Chuyên viên Đào tạo", "Thực tập sinh Nhân sự"));
        phongBanToChucVuMap.put("Phòng Công nghệ thông tin", Arrays.asList("Trưởng phòng IT", "Lập trình viên Backend", "Lập trình viên Frontend", "UI/UX Designer", "Quản trị hệ thống", "Nhân viên Hỗ trợ IT", "Thực tập sinh IT"));
        phongBanToChucVuMap.put("Phòng Kinh doanh", Arrays.asList("Trưởng phòng Kinh doanh", "Phó phòng Kinh doanh", "Nhân viên kinh doanh", "Trợ lý kinh doanh", "Nhân viên Tele-sales", "Thực tập sinh Kinh doanh"));
        phongBanToChucVuMap.put("Phòng Marketing", Arrays.asList("Trưởng phòng Marketing", "Chuyên viên Digital Marketing", "Chuyên viên Content Marketing", "Nhân viên thiết kế", "Chuyên viên SEO/SEM", "Thực tập sinh Marketing"));
        phongBanToChucVuMap.put("Ban Giám đốc", Arrays.asList("Tổng Giám đốc (CEO)", "Phó Tổng Giám đốc", "Giám đốc Tài chính (CFO)", "Giám đốc Công nghệ (CTO)", "Trợ lý Giám đốc"));
        phongBanToChucVuMap.put("Chờ phân công", Arrays.asList("Chưa có chức vụ")); // Dành cho P00

        // Tạo map ngược (Chức Vụ -> Tên Phòng) và danh sách tổng (allChucVuList)
        allChucVuList.clear();
        chucVuToPhongBanMap.clear();
        
        for (Map.Entry<String, List<String>> entry : phongBanToChucVuMap.entrySet()) {
            String tenPhong = entry.getKey();
            for (String chucVu : entry.getValue()) {
                chucVuToPhongBanMap.put(chucVu, tenPhong);
                allChucVuList.add(chucVu);
            }
        }
        
        // Sắp xếp danh sách tổng cho dễ nhìn
        Collections.sort(allChucVuList);
    }
    private boolean isNumeric(String str) {
        if (str == null) return false;
            return str.matches("\\d+");
    }
    
    @FXML
    private void nhansu_themAction() {
        String maNV = nhansu_txma.getText().trim();
        String hoTen = nhansu_txten.getText().trim();
        String gioiTinh = nhansu_cbogioitinh.getValue();
        LocalDate ngaySinh = nhansu_datengaysinh.getValue();
        String cccd = nhansu_txcccd.getText().trim();
        String email = nhansu_txemail.getText().trim();
        String sdt = nhansu_txsdt.getText().trim();
        PhongBan selectedPB = nhansu_cbmaPB.getValue();
        if (selectedPB == null) { // Kiểm tra nếu chưa chọn
             canhbao.canhbao("Thông tin không được bỏ trống", "Vui lòng chọn 1 nhân viên.");
            return;
        }
        String maPhongBan = selectedPB.getMaPhong(); // Lấy mã từ đối tượng
        String chucVu = nhansu_cbchucvu.getValue();

        if (maNV.isEmpty() || hoTen.isEmpty() || ngaySinh == null || maPhongBan == null) {
            // Cập nhật lại thông báo lỗi một chút cho rõ ràng
            canhbao.canhbao("Thông tin không được bỏ trống", "Vui lòng nhập đầy đủ Mã NV, Họ tên, Ngày sinh và Phòng ban.");
            return;
        }

        if (maNV.length() != 5) {
            canhbao.canhbao("Sai định dạng", "Mã nhân viên phải có đúng 5 ký tự.");
            return;
        }

        // Kiểm tra CCCD (nếu người dùng có nhập)
        if (!cccd.isEmpty()) {
            if (!isNumeric(cccd) || cccd.length() != 12) {
                canhbao.canhbao("Sai định dạng", "CCCD (nếu nhập) phải là 12 ký tự số.");
                return;
            }
        }

        // Kiểm tra SĐT (nếu người dùng có nhập)
        if (!sdt.isEmpty()) {
            if (!isNumeric(sdt) || sdt.length() != 10) {
                canhbao.canhbao("Sai định dạng", "Số điện thoại (nếu nhập) phải là 10 ký tự số.");
                return;
            }
    }
        
        if (maNV.isEmpty() || hoTen.isEmpty() || ngaySinh == null || maPhongBan == null) {
            canhbao.canhbao("Thông tin không được bỏ trống", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        
        // Kiểm tra xem chức vụ có thuộc phòng ban đã chọn không
        if (chucVu == null || chucVu.isEmpty() || chucVu.contains("Chưa có chức vụ")) {
             canhbao.canhbao("Thiếu chức vụ", "Vui lòng chọn chức vụ cho nhân sự.");
            return;
        }
        
        String tenPhongTuChucVu = chucVuToPhongBanMap.get(chucVu);
        PhongBan pbDaChon = selectedPB;
        
        if (pbDaChon == null || !pbDaChon.getTenPhong().equals(tenPhongTuChucVu)) {
             canhbao.canhbao("Dữ liệu không khớp", "Chức vụ \"" + chucVu + "\" không thuộc phòng \"" + (pbDaChon != null ? pbDaChon.getTenPhong() : "N/A") + "\".");
            return;
        }

        if (dataService.timNhanSuTheoMa(maNV) != null) {
            canhbao.canhbao("Trùng mã", "Mã nhân sự \"" + maNV + "\" đã tồn tại.");
            return;
        }
        
        NhanSu ns = new NhanSu(maNV, hoTen, gioiTinh, ngaySinh, cccd, email, sdt, maPhongBan, chucVu);

        // Thêm thông qua DataService để đồng bộ
        if (dataService.addNhanSu(ns)) {
            canhbao.thongbao("Thành công", "Đã thêm nhân sự \"" + hoTen + "\".");
             // Xóa trắng các ô nhập liệu
            nhansu_txma.clear();
            nhansu_txten.clear();
            nhansu_cbogioitinh.getSelectionModel().clearSelection();
            nhansu_datengaysinh.setValue(null);
            nhansu_txcccd.clear();
            nhansu_txemail.clear();
            nhansu_txsdt.clear();
            nhansu_cbmaPB.getSelectionModel().clearSelection();
            nhansu_cbchucvu.getSelectionModel().clearSelection();
            nhansu_cbchucvu.setItems(allChucVuList);
        }
    }

    @FXML 
    private void nhansu_xoaAction() { 
         NhanSu selectedNhanSu = nhansu_tbnhansu.getSelectionModel().getSelectedItem();
        if (selectedNhanSu == null) {
            canhbao.canhbao("Chưa chọn nhân viên", "Vui lòng chọn một hàng để xóa.");
            return;
        }
        
        boolean dongY = canhbao.xacNhan(
                    "Xác nhận xóa",
                    "Bạn có chắc chắn muốn xóa nhân viên: " + selectedNhanSu.getHoTen() + "?",
                    "Hành động này không thể hoàn tác."
        );
        
        if (dongY) {
            dataService.deleteNhanSu(selectedNhanSu);
            canhbao.thongbao("Thành công", "Đã xóa nhân viên.");
        }
    }
    
    @FXML 
    private void nhansu_suaAction() throws IOException {
        NhanSu nsselected = nhansu_tbnhansu.getSelectionModel().getSelectedItem();
        if(nsselected == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION,"Chọn 1 hàng để sửa", ButtonType.YES);
            a.setTitle("Thong Tin");
            a.showAndWait();
            return;
        }
         
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("suanhansu.fxml"));
            Parent root = loader.load();

            SuaNhanSu controller = loader.getController();
            controller.setData(nsselected);

            nhansu_btsua.getScene().setRoot(root);

        } catch (IOException e) {
            // In chi tiết lỗi ra Console để bạn xem
            e.printStackTrace(); 
        
            // Hiển thị một cảnh báo trực tiếp trên giao diện
            canhbao.canhbao("Lỗi Tải Giao Diện", 
                "Không thể tải file suanhansu.fxml.\n" +
                "Vui lòng kiểm tra cửa sổ Output/Console để xem chi tiết lỗi.");
        }
    }
    
    @FXML 
    private void nhansu_timkiemAction() throws IOException { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timkiemnhansu.fxml"));
        Parent root = loader.load();
    
        // Lấy controller của màn hình "timkiemnhansu"
        TimKiemNhanSu controller = loader.getController();
    
        // Lấy danh sách mã phòng ban từ dsMaPhongForCombo đã có sẵn
        // Và truyền toàn bộ danh sách nhân sự từ dataService
        controller.setData(dataService.getDsNhanSu(), dataService.getDsPhongBan(), allChucVuList);
    
        // Lấy Scene hiện tại và set root mới
        nhansu_bttimkiem.getScene().setRoot(root);
    }
    
    @FXML
    private void nhansu_quaylaiAction() throws IOException {
        App.setRoot("main");
    }
}
