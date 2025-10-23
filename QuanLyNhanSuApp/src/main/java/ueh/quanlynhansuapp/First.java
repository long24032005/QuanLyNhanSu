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
    public void main_nhansuAction() throws IOException {
        App.setRoot("nhansu");
    } 
    @FXML
    public void main_phongbanAction() throws IOException {
        App.setRoot("phongban");
    } 
}
