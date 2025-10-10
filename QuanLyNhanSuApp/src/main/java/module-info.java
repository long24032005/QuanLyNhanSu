module ueh.quanlynhansuapp {
    requires javafx.controls;
    requires javafx.fxml;
        requires java.sql;
    opens ueh.quanlynhansuapp to javafx.fxml;
    exports ueh.quanlynhansuapp;
}
