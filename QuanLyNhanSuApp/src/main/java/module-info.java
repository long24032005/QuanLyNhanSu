module ueh.quanlynhansuapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens ueh.quanlynhansuapp to javafx.fxml;
    exports ueh.quanlynhansuapp;
}
