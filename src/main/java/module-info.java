module algo {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports coins.game;
    opens coins.game to javafx.fxml, javafx.base;
    exports coins.game.controllers;
    opens coins.game.controllers to javafx.fxml, javafx.base;
}