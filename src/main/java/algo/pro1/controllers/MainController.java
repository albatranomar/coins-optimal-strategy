package algo.pro1.controllers;

import algo.pro1.GameSettings;
import algo.pro1.util.Alerter;
import algo.pro1.util.FXMLUtil;
import algo.pro1.util.View;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane mainContainer;

    private GameSettings gameSettings;

    @FXML
    void initialize() {
        gameSettings = new GameSettings();
    }

    @FXML
    void onSettingsClicked() {
        try {
            Stage dialog = FXMLUtil.loadDialog(View.SETTINGS, (SettingsController controller) -> controller.inject(gameSettings));

            dialog.setTitle("Settings");
            dialog.show();
        } catch (IOException e) {
            Alerter.error("View Not Found", "Unable to load the settings pane!").show();
        }
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

    private <C> boolean setBody(View view, FXMLUtil.ControllerLambda<C> callback) {
        Pane pane;
        try {
            pane = FXMLUtil.load(view, callback);
        } catch (IOException e) {
            return false;
        }

        mainContainer.setCenter(pane);
        return true;
    }
}
