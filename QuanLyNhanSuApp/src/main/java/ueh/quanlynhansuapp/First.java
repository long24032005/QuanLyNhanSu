/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author lagia
 */
public class First {
    @FXML
    Button main_btnhansu;
    @FXML
    Button main_btphongban;
    
    @FXML
    public void main_nhansuAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang vào bảng nhân sự");
        App.setRoot("nhansu");
    } 
    @FXML
    public void main_phongbanAction() throws IOException {
        canhbao.thongbao("Thông báo", "bạn đang vào bảng phòng ban");
        App.setRoot("phongban");
    } 
}
