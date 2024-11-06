package algo.pro1.controllers;

import algo.pro1.GameSettings;
import algo.pro1.util.Alerter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SettingsController {
    @FXML
    private Label lblInfo;

    @FXML
    private TextArea tfCoins;

    @FXML
    private Button btLoad;

    @FXML
    private ToggleGroup tgEntryType;

    @FXML
    private RadioButton rbLoadFile;

    @FXML
    private RadioButton rbManually;

    @FXML
    private RadioButton rbRandomize;

    private GameSettings gameSettings;
    private int[] cachedCoins;

    @FXML
    void initialize() {
        rbRandomize.setOnAction(e -> refreshView());
        rbManually.setOnAction(e -> refreshView());
        rbLoadFile.setOnAction(e -> refreshView());
    }

    @FXML
    public void onLoadClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file that contains the coins");
        File selectedFile = fileChooser.showOpenDialog(btLoad.getScene().getWindow());
        if (selectedFile != null) {
            try (Scanner scanner = new Scanner(selectedFile)) {
                // Read the first line in the file
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    cachedCoins = convertLine(line);
                    if (cachedCoins != null) {
                        showCoins();
                        Alerter.info("The file loaded successfully").show();
                    }
                }
            } catch (FileNotFoundException ex) {
                Alerter.error("File Not Found", "Unable to find the selected file try again!").show();
            }
        }
    }

    @FXML
    public void onSaveClicked() {
        if (rbRandomize.isSelected()) {
            gameSettings.setEntryType(0);
            gameSettings.setCoins(null);
        } else if (rbManually.isSelected()) {
            int[] coins = convertLine(tfCoins.getText());
            if (coins == null) {
                return;
            }

            gameSettings.setEntryType(1);
            gameSettings.setCoins(coins);

        } else {
            if (cachedCoins == null || cachedCoins.length == 0 || cachedCoins.length % 2 != 0) {
                Alerter.error("Unloaded Coins", "Please load a valid sequence of coins!").show();
                return;
            }

            gameSettings.setEntryType(2);
            gameSettings.setCoins(cachedCoins);
        }
        Alerter.info("Game Settings have been saved").show();
        ((Stage) lblInfo.getScene().getWindow()).close();
    }

    public void inject(GameSettings gameSettings) {
        this.gameSettings = gameSettings;

        switch (gameSettings.getEntryType()) {
            case 0 -> rbRandomize.setSelected(true);
            case 1 -> rbManually.setSelected(true);
            case 2 -> rbLoadFile.setSelected(true);
        }

        refreshView();
    }

    private void refreshView() {
        if (rbRandomize.isSelected()) {
            lblInfo.setVisible(false);
            tfCoins.setVisible(false);
            btLoad.setVisible(false);
        } else if (rbManually.isSelected()) {
            lblInfo.setText("Enter the coins:");
            lblInfo.setVisible(true);

            if (gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                showCoins();
            }

            tfCoins.setVisible(true);
            tfCoins.setDisable(false);
            tfCoins.setEditable(true);

            btLoad.setVisible(false);
        } else if (rbLoadFile.isSelected()) {
            lblInfo.setText("Loaded Coins");
            lblInfo.setVisible(true);

            if (gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                showCoins();
            }

            tfCoins.setDisable(true);
            tfCoins.setEditable(false);
            tfCoins.setVisible(true);

            btLoad.setVisible(true);
        }
    }

    private int[] convertLine(String line) {
        String[] parts = line.split(",");

        if (parts.length == 0 || parts.length % 2 != 0) {
            Alerter.error("Invalid Number of coins! [ " + parts.length + " ]", "The number of coins must be greater than zero and even.").show();
            return null;
        }

        int[] coins = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            try {
                int coin = Integer.parseInt(part);
                if (coin <= 0) {
                    Alerter.error("The value of the coins is invalid", "A coin must be greater than zero. [ " + coin + " ]").show();
                    return null;
                }
                coins[i] = coin;
            } catch (NumberFormatException e) {
                Alerter.error("The value of the coins is invalid", "A coin must be a real number, no characters allowed. [ " + part + " ]").show();
                return null;
            }
        }

        return coins;
    }

    private void showCoins() {
        tfCoins.clear();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cachedCoins.length; i++) {
            int coin = cachedCoins[i];
            stringBuilder.append(coin);
            if (i != cachedCoins.length - 1) {
                stringBuilder.append(", ");
            }
        }

        tfCoins.setText(stringBuilder.toString());
    }
}