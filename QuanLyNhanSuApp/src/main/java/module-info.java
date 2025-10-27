module ueh.quanlynhansuapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens ueh.quanlynhansuapp to javafx.fxml;
    exports ueh.quanlynhansuapp;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
}
