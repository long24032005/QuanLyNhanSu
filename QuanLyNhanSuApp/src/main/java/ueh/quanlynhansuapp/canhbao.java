/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *S
 * @author lagia
 */
public class canhbao {
    public static void thongbao(String header, String noidung){
        Alert a = new Alert(Alert.AlertType.ERROR,noidung,ButtonType.OK);
        a.setTitle(header);
        a.showAndWait();
    }
}
