package algo.pro1.controllers;

import algo.pro1.GameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
    private GameSettings gameSettings;

    @FXML
    void initialize() {
        gameSettings = new GameSettings();
    }

    @FXML
    void onSettingsClicked() {

    }

    @FXML
    void onPlayerVsPlayerClicked() {

    }

    @FXML
    void onSimulateOptimalGameClicked() {

    }

    @FXML
    void onExitClicked() {
        System.exit(0);
    }
}
