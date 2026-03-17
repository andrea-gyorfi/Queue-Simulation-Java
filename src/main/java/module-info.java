module com.example.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.assignment2 to javafx.fxml;
    exports com.example.assignment2;
    exports com.example.assignment2.gui;
    opens com.example.assignment2.gui to javafx.fxml;
}