module algo {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports algo.pro1;
    opens algo.pro1 to javafx.fxml, javafx.base;
    exports algo.pro1.controllers;
    opens algo.pro1.controllers to javafx.fxml, javafx.base;
}