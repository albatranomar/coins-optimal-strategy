package algo.pro1.controllers;

import algo.pro1.GameSettings;
import algo.pro1.util.Alerter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// The Settings Pane Controller
// Handles the operation of changing and modifying the game settings
public class SettingsController {
    @FXML
    private Button btAddCoin;

    @FXML
    private Button btLoad;

    @FXML
    private Button btSubmitNumberOfCoins;

    @FXML
    private Label lblInfo;

    @FXML
    private RadioButton rbLoadFile;

    @FXML
    private RadioButton rbManually;

    @FXML
    private RadioButton rbRandomize;

    @FXML
    private Slider slider;

    @FXML
    private HBox sliderContainer;

    @FXML
    private TextField tfCoinEnter;

    @FXML
    private TextArea tfCoins;

    @FXML
    private TextField tfNumberOfCoins;

    @FXML
    private ToggleGroup tgEntryType;

    @FXML
    private VBox vbInputContainer;

    private GameSettings gameSettings;
    private int[] cachedCoins;
    private int cacheCount;

    // Accepts an injection from the parent controller
    public void inject(GameSettings gameSettings) {
        this.gameSettings = gameSettings;

        switch (gameSettings.getEntryType()) {
            case 0 -> rbRandomize.setSelected(true);
            case 1 -> rbManually.setSelected(true);
            case 2 -> rbLoadFile.setSelected(true);
        }

        refreshView();
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

                    if (cachedCoins == null) return;

                    showCoins();
                    Alerter.info("The file loaded successfully").show();
                } else {
                    Alerter.error("The file provided is empty!").show();
                }
            } catch (FileNotFoundException ex) {
                Alerter.error("File Not Found", "Unable to find the selected file try again!").show();
            }
        }
    }

    // Saves the game settings
    @FXML
    public void onSaveClicked() {
        if (rbRandomize.isSelected()) {
            gameSettings.setEntryType(GameSettings.RANDOMIZE);
            gameSettings.setCoins(new int[(int) slider.getValue()]);
        } else if (rbManually.isSelected()) {
            int n = cachedCoins == null ? 0 : cachedCoins.length;
            if (n == 0 || n % 2 != 0) {
                Alerter.error("Invalid Number of coins! [ " + n + " ]", "The number of coins must be greater than zero and even.").show();
                return;
            }

            if (cacheCount != n) {
                Alerter.error("Missing Coins! [ " + cacheCount + " of " + n + " ]", "You need to enter coin by coin until there is " + n + " coins.").show();
                return;
            }

            gameSettings.setEntryType(GameSettings.MANUALLY);
            gameSettings.setCoins(cachedCoins);

        } else {
            if (cachedCoins == null || cachedCoins.length == 0 || cachedCoins.length % 2 != 0) {
                Alerter.error("Unloaded Coins", "Please load a valid sequence of coins!").show();
                return;
            }

            gameSettings.setEntryType(GameSettings.FILELOADED);
            gameSettings.setCoins(cachedCoins);
        }
        Alerter.info("Game Settings have been saved").show();
        ((Stage) lblInfo.getScene().getWindow()).close();
    }

    @FXML
    public void ignoreNonDigit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().isArrowKey()) {
            TextField tf = (TextField) keyEvent.getTarget();
            keyEvent.consume();

            tf.setText(tf.getText().replaceAll("\\D", ""));
            tf.positionCaret(tf.getText().length());
        }
    }

    // Handles what is shown in the screen according to the selected entry type
    @FXML
    private void refreshView() {
        if (rbRandomize.isSelected()) {
            sliderContainer.setVisible(true);

            if (gameSettings.getEntryType() == GameSettings.RANDOMIZE && gameSettings.getCoins() != null) {
                slider.setValue(gameSettings.getCoins().length);
            }

            vbInputContainer.setVisible(false);
        } else if (rbManually.isSelected()) {
            lblInfo.setText("Manually Inserted Coins");

            sliderContainer.setVisible(false);

            tfNumberOfCoins.setDisable(false);
            btSubmitNumberOfCoins.setDisable(false);

            tfCoinEnter.setDisable(true);
            btAddCoin.setDisable(true);

            cacheCount = 0;
            cachedCoins = null;
            tfCoins.clear();

            btLoad.setVisible(false);

            if (gameSettings.getEntryType() != 0 && gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                showCoins();
            }

            vbInputContainer.setVisible(true);
        } else if (rbLoadFile.isSelected()) {
            lblInfo.setText("Loaded Coins");

            sliderContainer.setVisible(false);

            tfNumberOfCoins.setDisable(true);
            tfCoinEnter.setDisable(true);
            btSubmitNumberOfCoins.setDisable(true);
            btAddCoin.setDisable(true);

            btLoad.setVisible(true);
            tfCoins.clear();

            if (gameSettings.getEntryType() != 0 && gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                showCoins();
            }

            vbInputContainer.setVisible(true);
        }
    }

    // Converts a string of coins to an array
    @FXML
    public void onNumberOfCoinsSubmitted() {
        tfCoinEnter.clear();
        tfCoins.clear();
        cachedCoins = null;
        cacheCount = 0;
        tfCoinEnter.setDisable(true);
        btAddCoin.setDisable(true);

        String numberOfCoinsText = tfNumberOfCoins.getText();
        try {
            int numberOfCoins = Integer.parseInt(numberOfCoinsText);

            if (numberOfCoins >= 0 && numberOfCoins % 2 == 0) {
                cachedCoins = new int[numberOfCoins];

                tfNumberOfCoins.setDisable(false);
                btSubmitNumberOfCoins.setDisable(false);
                cacheCount = 0;

                tfCoinEnter.setDisable(false);
                btAddCoin.setDisable(false);
            } else {
                Alerter.error("Invalid Coin Number", "Please enter an even number >= 0.").show();
            }
        } catch (NumberFormatException e) {
            Alerter.error("Invalid Coin Number", "Invalid input! Enter an integer.").show();
        }
    }

    @FXML
    public void onAddCoinClicked() {
        String coinText = tfCoinEnter.getText();
        try {
            int coin = Integer.parseInt(coinText);

            if (coin > 0) {
                cachedCoins[cacheCount++] = coin;

                tfCoins.appendText(String.valueOf(coin));
                if (cacheCount != cachedCoins.length) {
                    tfCoins.appendText(", ");
                }

                tfCoinEnter.clear();

                // Check if all numbers have been entered
                if (cacheCount == cachedCoins.length) {
                    tfCoinEnter.setDisable(true);
                    btAddCoin.setDisable(true);
                    Alerter.info("All numbers entered successfully!").show();
                }
            } else {
                Alerter.error("Invalid Coin Value", "Please enter a number greater than 0.").show();
            }
        } catch (NumberFormatException e) {
            Alerter.error("Invalid Coin Number", "Invalid input! Enter an integer.").show();
        }
    }

    // Converts a string of coins values to an array of int
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

    // display the coins on the screen
    private void showCoins() {
        tfNumberOfCoins.setText(String.valueOf(cachedCoins.length));
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