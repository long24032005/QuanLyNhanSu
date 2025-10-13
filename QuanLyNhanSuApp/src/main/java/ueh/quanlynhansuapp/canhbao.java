/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ueh.quanlynhansuapp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author lagia
 */
public class canhbao {
    public static void canhbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }

    public static void thongbao(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
}
