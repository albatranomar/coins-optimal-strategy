package algo.pro1.controllers;

import algo.pro1.Game;
import algo.pro1.Game.*;
import algo.pro1.GameSettings;
import algo.pro1.util.Alerter;
import algo.pro1.util.FXMLUtil;
import algo.pro1.util.SoundPlayer;
import algo.pro1.util.View;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Random;

// The entry point Controller
public class MainController {
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

        SoundPlayer.init();
    }

    private void loadPlayground(String title, Game game, String playerName1, String playerName2) {
        // Load the Playground view, and inject the game instance and the players names to it
        Stage dialog = FXMLUtil.loadDialog(View.PLAYGROUND, (PlaygroundController controller) -> controller.inject(game, playerName1, playerName2));

        // if unable to load the view, just return since the loadDialog handles the alert
        if (dialog == null) return;

        dialog.setTitle(title);
        dialog.show();
    }

    @FXML
    void onPlayerVsPlayerClicked() {
        // Create a new game session
        Game game = new Game(GameType.PvP, gameSettings);

        String name1 = tfPlayerOneName.getText();
        String name2 = tfPlayerTwoName.getText();

        if (name1.isEmpty() || name2.isEmpty() || name1.equals(name2)) {
            Alerter.error("Invalid players name", "Please provide a non empty and different name foreach of the players").show();
            return;
        }

        // Set the player turn to the selected one
        // Or Pick random player if not specified.
        if (cbPlayerOne.isSelected()) {
            game.setTurn(Player.PLAYER1);
        } else if (cbPlayerTwo.isSelected()) {
            game.setTurn(Player.PLAYER2);
        } else {
            game.setTurn(new Random().nextBoolean() ? Player.PLAYER1 : Player.PLAYER2);
        }

        loadPlayground("PvP", game, name1, name2);
    }

    @FXML
    public void onPlayerVsComputerClicked() {
        Game game = new Game(GameType.PvC, gameSettings);

        String playerName = tfYourName.getText();
        String computerName = tfComputerName.getText();

        if (playerName.isEmpty() || computerName.isEmpty() || playerName.equals(computerName)) {
            Alerter.error("Invalid players name", "Please provide a non empty and different name foreach of the players").show();
            return;
        }

        // Set the player turn to the selected one
        // Or Pick random player if not specified.
        if (cbYouStart.isSelected()) {
            game.setTurn(Player.PLAYER1);
        } else if (cbComputerStart.isSelected()) {
            game.setTurn(Player.PLAYER2);
        } else {
            game.setTurn(new Random().nextBoolean() ? Player.PLAYER1 : Player.PLAYER2);
        }

        loadPlayground("PvC", game, playerName, computerName);
    }

    @FXML
    void onSimulateOptimalGameClicked() {
        Game game = new Game(GameType.CvC, gameSettings);

        loadPlayground("Optimal Game Simulation", game, "Smart & Lucky", "Just Smart");
    }

    @FXML
    void onExitClicked() {
        SoundPlayer.playClick();
        System.exit(0);
    }

    @FXML
    void onSettingsClicked() {
        SoundPlayer.playClick();
        Stage dialog = FXMLUtil.loadDialog(View.SETTINGS, (SettingsController controller) -> controller.inject(gameSettings));

        // if unable to load the view, just return since the loadDialog handles the alert
        if (dialog == null) return;

        dialog.setTitle("Settings");
        dialog.show();
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
}
