package coins.game.controllers;

import coins.game.GameSettings;
import coins.game.GameSettings.*;
import coins.game.util.Alerter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
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
            case RANDOM -> rbRandomize.setSelected(true);
            case MANUAL -> rbManually.setSelected(true);
            case FILE -> rbLoadFile.setSelected(true);
        }

        refreshView();
    }

    @FXML
    public void onLoadClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text and CSV Files (*.txt, *.csv)", "*.txt", "*.csv"));
        fileChooser.setTitle("Select a file that contains the coins");
        File selectedFile = fileChooser.showOpenDialog(btLoad.getScene().getWindow());
        if (selectedFile != null) {
            cachedCoins = loadCoins(selectedFile);

            if (cachedCoins == null) return;

            showCoins();
            Alerter.info("The file loaded successfully").show();
        }
    }

    // Saves the game settings
    @FXML
    public void onSaveClicked() {
        if (rbRandomize.isSelected()) {
            gameSettings.setEntryType(EntryType.RANDOM);
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

            gameSettings.setEntryType(EntryType.MANUAL);
            gameSettings.setCoins(cachedCoins);

        } else {
            if (cachedCoins == null || cachedCoins.length == 0 || cachedCoins.length % 2 != 0) {
                Alerter.error("Unloaded Coins", "Please load a valid sequence of coins!").show();
                return;
            }

            gameSettings.setEntryType(EntryType.FILE);
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

            if (gameSettings.getEntryType() == EntryType.RANDOM && gameSettings.getCoins() != null) {
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

            if (gameSettings.getEntryType() != EntryType.RANDOM && gameSettings.getCoins() != null) {
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

            if (gameSettings.getEntryType() != EntryType.RANDOM && gameSettings.getCoins() != null) {
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

    private int[] loadCoins(File file) {
        try (Scanner scan = new Scanner(file)) {
            int numberOfCoins = scan.nextInt();

            if (numberOfCoins == 0 || numberOfCoins % 2 != 0) {
                Alerter.error("Invalid Number of coins! [ " + numberOfCoins + " ]", "The number of coins must be greater than zero and even.").show();
                return null;
            }

            int[] coins = new int[numberOfCoins];
            for (int i = 0; i < numberOfCoins; i++) {
                int coin = scan.nextInt();
                if (coin <= 0) {
                    Alerter.error("The value of the coins is invalid", "A coin must be greater than zero. [ " + coin + " ]").show();
                    return null;
                }

                coins[i] = coin;
            }

            return coins;
        } catch (InputMismatchException e) {
            Alerter.error("The value of the coins is invalid", "A coin must be a real number, no characters allowed.").show();
        } catch (NoSuchElementException e) {
            Alerter.error("Unexpected end of file while reading the coins!", e.getMessage()).show();
        } catch (IOException e) {
            Alerter.error("Something went wrong!", e.getMessage()).show();
        }

        return null;
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