package algo.pro1.controllers;

import algo.pro1.Game;
import algo.pro1.GameSettings;
import algo.pro1.util.Alerter;
import algo.pro1.util.FXMLUtil;
import algo.pro1.util.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class MainController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    private TextField tfPlayerOneName;

    @FXML
    private TextField tfPlayerTwoName;

    @FXML
    private CheckBox cbPlayerOne;

    @FXML
    private CheckBox cbPlayerTwo;

    @FXML
    public TextField tfYourName;

    @FXML
    public CheckBox cbYouStart;

    @FXML
    public TextField tfComputerName;

    @FXML
    public CheckBox cbComputerStart;

    private GameSettings gameSettings;

    @FXML
    void initialize() {
        gameSettings = new GameSettings();
    }

    @FXML
    void onPlayerVsPlayerClicked() {
        Game game = new Game(Game.PvP, gameSettings);

        String name1 = tfPlayerOneName.getText();
        String name2 = tfPlayerTwoName.getText();

        if (name1.isEmpty() || name2.isEmpty() || name1.equals(name2)) {
            Alerter.error("Invalid players name", "Please provide a non empty and different name foreach of the players").show();
            return;
        }


        if (cbPlayerOne.isSelected()) {
            game.setTurn(Game.PLAYER1);
        } else if (cbPlayerTwo.isSelected()) {
            game.setTurn(Game.PLAYER2);
        } else {
            game.setTurn(new Random().nextInt(2) + 1);
        }

        try {
            Stage dialog = FXMLUtil.loadDialog(View.PLAYGROUND, (PlaygroundController controller) -> controller.inject(game, name1, name2));

            dialog.setTitle("PvP");
            dialog.show();
        } catch (IOException e) {
            Alerter.error("View Not Found", "Unable to load the playground pane!").show();
        }
    }

    @FXML
    void onSelectPlayerOne() {
        if (cbPlayerOne.isSelected()) {
            cbPlayerTwo.setSelected(false);
        }
    }

    @FXML
    void onSelectPlayerTwo() {
        if (cbPlayerTwo.isSelected()) {
            cbPlayerOne.setSelected(false);
        }
    }

    @FXML
    public void onPlayerVsComputerClicked() {
        Game game = new Game(Game.PvC, gameSettings);

        String playerName = tfYourName.getText();
        String computerName = tfComputerName.getText();

        if (playerName.isEmpty() || computerName.isEmpty() || playerName.equals(computerName)) {
            Alerter.error("Invalid players name", "Please provide a non empty and different name foreach of the players").show();
            return;
        }

        if (cbYouStart.isSelected()) {
            game.setTurn(Game.PLAYER1);
        } else if (cbComputerStart.isSelected()) {
            game.setTurn(Game.PLAYER2);
        } else {
            game.setTurn(new Random().nextInt(2) + 1);
        }

        try {
            Stage dialog = FXMLUtil.loadDialog(View.PLAYGROUND, (PlaygroundController controller) -> controller.inject(game, playerName, computerName));

            dialog.setTitle("PvC");
            dialog.show();
        } catch (IOException e) {
            Alerter.error("View Not Found", "Unable to load the playground pane!").show();
        }
    }

    @FXML
    public void onSelectYouStart() {
        if (cbYouStart.isSelected()) {
            cbComputerStart.setSelected(false);
        }
    }

    @FXML
    public void onSelectComputerStart() {
        if (cbComputerStart.isSelected()) {
            cbYouStart.setSelected(false);
        }
    }

    @FXML
    void onSimulateOptimalGameClicked() {
        Game game = new Game(Game.CvC, gameSettings);

        try {
            Stage dialog = FXMLUtil.loadDialog(View.PLAYGROUND, (PlaygroundController controller) -> controller.inject(game, "Smart & Lucky", "Just Smart"));

            dialog.setTitle("CvC");
            dialog.show();
        } catch (IOException e) {
            Alerter.error("View Not Found", "Unable to load the playground pane!").show();
        }
    }

    @FXML
    void onExitClicked() {
        System.exit(0);
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
}
