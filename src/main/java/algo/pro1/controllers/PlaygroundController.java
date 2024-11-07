package algo.pro1.controllers;

import algo.pro1.Coin;
import algo.pro1.Game;
import algo.pro1.util.Alerter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Optional;

public class PlaygroundController {
    @FXML
    private Button btShowTable;

    @FXML
    private FlowPane coinsContainer;

    @FXML
    private Label lblPlayerOneCoins;

    @FXML
    private Label lblPlayerOneGain;

    @FXML
    private Label lblPlayerOneName;

    @FXML
    private Label lblPlayerTwoCoins;

    @FXML
    private Label lblPlayerTwoGain;

    @FXML
    private Label lblPlayerTwoName;

    private Game game;

    public void inject(Game game, String playerOneName, String playerTwoName) {
        lblPlayerOneName.setText(playerOneName);
        lblPlayerTwoName.setText(playerTwoName);

        this.game = game;

        game.start();

        if (game.getType() != Game.CvC) {
            btShowTable.setVisible(false);
        }

        int[] coins = game.getSettings().getCoins();
        for (int i = 0; i < coins.length; i++) {
            Coin coin = new Coin(coins[i], i == 0 || i == coins.length - 1);
            coinsContainer.getChildren().add(coin);

            if (!coin.isClickAble()) coin.setOpacity(0.4);

            coin.setOnMouseClicked(event -> handleOnCoinClicked(coin));
        }
    }

    @FXML
    void onGoBackClicked() {
        Stage stage = (Stage) coinsContainer.getScene().getWindow();

        if (game.isGameOver()) {
            stage.close();
            return;
        }

        Alert confirmation = Alerter.confirm("The game is not over yet", "Are you sure leave?");
        Optional<ButtonType> response = confirmation.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            ((Stage) coinsContainer.getScene().getWindow()).close();
        }
    }

    @FXML
    void onShowTableClicked() {

    }

    private void handleOnCoinClicked(Coin coin) {
        if (!coin.isClickAble()) return;

        int player = game.getTurn();

        boolean pickedFirst = false;
        if (coin == coinsContainer.getChildren().getFirst()) {
            pickedFirst = true;
            game.pickFirst();
        } else {
            game.pickLast();
        }

        coinsContainer.getChildren().remove(coin);
        addCoinToPlayer(coin, player);

        if (game.isGameOver()) {
            int winner = game.getWinner();
            String msg;
            if (winner == Game.PLAYER1) {
                msg = lblPlayerOneName.getText() + " WON THE GAME!";
            } else if (winner == Game.PLAYER2) {
                msg = lblPlayerTwoName.getText() + " WON THE GAME!";
            } else {
                msg = "Its a draw!";
            }
            Alerter.info("GAME IS OVER", msg).show();
            return;
        }

        Coin nextCoin;
        if (pickedFirst) {
            nextCoin = (Coin) coinsContainer.getChildren().getFirst();
        } else {
            nextCoin = (Coin) coinsContainer.getChildren().getLast();
        }

        nextCoin.setOpacity(1);
        nextCoin.setClickAble(true);
    }

    private void addCoinToPlayer(Coin coin, int player) {
        Label coinsLabel = player == Game.PLAYER1 ? lblPlayerOneCoins : lblPlayerTwoCoins;

        String txtCoins = coinsLabel.getText().equals("N/A") ? "" : coinsLabel.getText();

        if (!txtCoins.isEmpty()) {
            txtCoins += ", ";
        }
        txtCoins += coin.getValue();

        coinsLabel.setText(txtCoins);

        Label gainLabel = player == Game.PLAYER1 ? lblPlayerOneGain : lblPlayerTwoGain;
        game.incrementPlayerGainBy(player, coin.getValue());
        gainLabel.setText(String.valueOf(game.getPlayerGain(player)));
    }
}
