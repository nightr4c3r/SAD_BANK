module com.example.loginpf {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens com.example.loginpf to javafx.fxml;
    exports com.example.loginpf;
    exports com.example.loginpf.Controllers;
    opens com.example.loginpf.Controllers to javafx.fxml;
    // Allow JavaFX reflective access to model bean properties for TableView/PropertyValueFactory
    opens com.example.loginpf.Model to javafx.base;
}