module com.example.fxmaven {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.fxmaven to javafx.fxml;
    exports com.example.fxmaven;
}