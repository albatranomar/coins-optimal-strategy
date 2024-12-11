package coins.game.controllers;

import coins.game.GameSettings;
import coins.game.GameSettings.*;
import coins.game.util.Alerter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SettingsController {
    @FXML
    private Button btAddCoin;

    @FXML
    private Button btSave;

    @FXML
    private Label lblCoinByCoinError;

    @FXML
    private Label lblManualNumberOfCoinsError;

    @FXML
    private Label lblMaxError;

    @FXML
    private Label lblMinError;

    @FXML
    private Label lblRandomNumberOfCoinsError;

    @FXML
    private TextArea taCoins;

    @FXML
    private TextArea taFileCoins;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabFile;

    @FXML
    private Tab tabManual;

    @FXML
    private Tab tabRandom;

    @FXML
    private TextField tfCoinByCoin;

    @FXML
    private TextField tfFileNumberOfCoins;

    @FXML
    private TextField tfManualNumberOfCoins;

    @FXML
    private TextField tfMax;

    @FXML
    private TextField tfMin;

    @FXML
    private TextField tfRandomNumberOfCoins;

    private GameSettings gameSettings;
    private int[] cachedCoins;
    private int cacheCount;

    // Accepts an injection from the parent controller
    public void inject(GameSettings gameSettings) {
        this.gameSettings = gameSettings;

        switch (gameSettings.getEntryType()) {
            case RANDOM -> tabPane.getSelectionModel().select(tabRandom);
            case MANUAL -> tabPane.getSelectionModel().select(tabManual);
            case FILE -> tabPane.getSelectionModel().select(tabFile);
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> refreshView());

        refreshView();
    }

    @FXML
    void onLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text and CSV Files (*.txt, *.csv)", "*.txt", "*.csv"));
        fileChooser.setTitle("Select a file that contains the coins");
        File selectedFile = fileChooser.showOpenDialog(tabPane.getScene().getWindow());
        if (selectedFile != null) {
            cachedCoins = loadCoins(selectedFile);

            if (cachedCoins == null) return;

            showCoins(tfFileNumberOfCoins, taFileCoins);
            Alerter.info("The file loaded successfully").show();
        }
    }

    @FXML
    void onRandomNumberOfCoins(KeyEvent event) {
        ignoreNonDigit(event);
        lblRandomNumberOfCoinsError.setText("");

        try {
            int numberOfCoins = Integer.parseInt(tfRandomNumberOfCoins.getText());
            if (numberOfCoins <= 0 || numberOfCoins % 2 != 0) {
                lblRandomNumberOfCoinsError.setText("Must be greater than Zero and an Even number");
            }
        } catch (NumberFormatException e) {
            lblRandomNumberOfCoinsError.setText("Enter a valid Number");
        }
    }

    @FXML
    void onMax(KeyEvent event) {
        ignoreNonDigit(event);
        lblMaxError.setText("");

        try {
            int max = Integer.parseInt(tfMax.getText());
            if (max < 1 || max > 500) {
                lblMaxError.setText("Must be in range [1, 500]");
            }
        } catch (NumberFormatException e) {
            lblMaxError.setText("Enter a valid Number");
        }
    }

    @FXML
    void onMin(KeyEvent event) {
        ignoreNonDigit(event);
        lblMinError.setText("");

        try {
            int min = Integer.parseInt(tfMin.getText());
            if (min < 1 || min > 500) {
                lblMinError.setText("Must be in range [1, 500]");
            }
        } catch (NumberFormatException e) {
            lblMinError.setText("Enter a valid Number");
        }
    }

    @FXML
    void onManualNumberOfCoins(KeyEvent event) {
        ignoreNonDigit(event);
        lblManualNumberOfCoinsError.setText("");

        try {
            int numberOfCoins = Integer.parseInt(tfManualNumberOfCoins.getText());
            if (numberOfCoins <= 0 || numberOfCoins % 2 != 0) {
                lblManualNumberOfCoinsError.setText("Must be greater than Zero and an Even number");
                btAddCoin.setDisable(true);
                tfCoinByCoin.setDisable(true);
                taCoins.clear();
                return;
            }

            cachedCoins = new int[numberOfCoins];
            cacheCount = 0;

            tfCoinByCoin.setDisable(false);
            btAddCoin.setDisable(false);
            taCoins.clear();
        } catch (NumberFormatException e) {
            lblManualNumberOfCoinsError.setText("Enter a valid Number");
            btAddCoin.setDisable(true);
            tfCoinByCoin.setDisable(true);
            taCoins.clear();
        }
    }

    @FXML
    void onCoinByCoin(KeyEvent event) {
        ignoreNonDigit(event);
        lblCoinByCoinError.setText("");

        try {
            int coin = Integer.parseInt(tfCoinByCoin.getText());
            btAddCoin.setDisable(false);
        } catch (NumberFormatException e) {
            if (event.getCode() == KeyCode.ENTER) return;
            lblCoinByCoinError.setText("Enter a valid Number");
            btAddCoin.setDisable(true);
        }
    }

    @FXML
    void onAddCoin() {
        if (btAddCoin.isDisable()) return;

        String coinText = tfCoinByCoin.getText();
        try {
            int coin = Integer.parseInt(coinText);

            cachedCoins[cacheCount++] = coin;

            taCoins.appendText(String.valueOf(coin));
            if (cacheCount != cachedCoins.length) {
                taCoins.appendText(", ");
            }

            tfCoinByCoin.clear();

            // Check if all numbers have been entered
            if (cacheCount == cachedCoins.length) {
                tfCoinByCoin.setDisable(true);
                btAddCoin.setDisable(true);
                Alerter.info("All numbers entered successfully!").show();
            }

        } catch (NumberFormatException e) {
            Alerter.error("Invalid Coin Number", "Invalid input! Enter an integer.").show();
        }
    }

    @FXML
    void onSave() {
        if (tabRandom.isSelected()) {
            try {
                int numberOfCoins = Integer.parseInt(tfRandomNumberOfCoins.getText());
                int max = Integer.parseInt(tfMax.getText());
                int min = Integer.parseInt(tfMin.getText());

                if (min > max) {
                    Alerter.error("Invalid Min-Max values", "the min value must be less or equal to the max value").show();
                    return;
                }

                gameSettings.setEntryType(EntryType.RANDOM);
                gameSettings.setCoins(new int[numberOfCoins]);
                gameSettings.setMax(max);
                gameSettings.setMin(min);
            } catch (NumberFormatException e) {
                Alerter.error("Invalid Number Provided!", "The number of coins, min, and max must be valid integers").show();
                return;
            }
        } else if (tabManual.isSelected()) {
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
        } else if (tabFile.isSelected()) {
            if (cachedCoins == null || cachedCoins.length == 0 || cachedCoins.length % 2 != 0) {
                Alerter.error("Unloaded Coins", "Please load a valid sequence of coins!").show();
                return;
            }

            gameSettings.setEntryType(EntryType.FILE);
            gameSettings.setCoins(cachedCoins);
        }
        Alerter.info("Game Settings have been saved").show();
        ((Stage) tabPane.getScene().getWindow()).close();
    }

    private void refreshView() {
        lblManualNumberOfCoinsError.setText("");
        lblCoinByCoinError.setText("");
        lblMinError.setText("");
        lblMaxError.setText("");
        lblRandomNumberOfCoinsError.setText("");

        if (tabRandom.isSelected()) {
            if (gameSettings.getEntryType() == EntryType.RANDOM && gameSettings.getCoins() != null) {
                tfRandomNumberOfCoins.setText(gameSettings.getCoins().length + "");
                tfMax.setText(gameSettings.getMax() + "");
                tfMin.setText(gameSettings.getMin() + "");
            }
        } else if (tabManual.isSelected()) {
            cacheCount = 0;
            cachedCoins = null;
            taCoins.clear();

            if (gameSettings.getEntryType() == EntryType.MANUAL && gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                cacheCount = cachedCoins.length;
                showCoins(tfManualNumberOfCoins, taCoins);
                tfCoinByCoin.setDisable(true);
                btAddCoin.setDisable(true);
            }
        } else if (tabFile.isSelected()) {
            taFileCoins.clear();

            if (gameSettings.getEntryType() == EntryType.FILE && gameSettings.getCoins() != null) {
                cachedCoins = gameSettings.getCoins();
                showCoins(tfFileNumberOfCoins, taFileCoins);
            }
        }
    }

    // display the coins on the screen
    private void showCoins(TextField numOfCoins, TextArea coins) {
        numOfCoins.setText(String.valueOf(cachedCoins.length));
        coins.clear();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cachedCoins.length; i++) {
            int coin = cachedCoins[i];
            stringBuilder.append(coin);
            if (i != cachedCoins.length - 1) {
                stringBuilder.append(", ");
            }
        }

        coins.setText(stringBuilder.toString());
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

    private void ignoreNonDigit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().isArrowKey()) {
            TextField tf = (TextField) keyEvent.getTarget();
            keyEvent.consume();

            tf.setText(tf.getText().replaceAll("\\D", ""));
            tf.positionCaret(tf.getText().length());
        }
    }
}
