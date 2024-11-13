package algo.pro1.controllers;

import algo.pro1.Coin;
import algo.pro1.Game;
import algo.pro1.Game.*;
import algo.pro1.util.Alerter;
import algo.pro1.util.FXMLUtil;
import algo.pro1.util.SoundPlayer;
import algo.pro1.util.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PlaygroundController {
    @FXML
    private Button btNextMove;

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

    @FXML
    private StackPane playerOneBoard;

    @FXML
    private StackPane playerTwoBoard;

    private Game game;

    public void inject(Game game, String playerOneName, String playerTwoName) {
        lblPlayerOneName.setText(playerOneName);
        lblPlayerTwoName.setText(playerTwoName);

        this.game = game;

        initGame();
    }

    // Starts the game & Initialize the view accordingly
    private void initGame() {
        game.start();

        // Adjust the view based on the game type
        switch (game.getType()) {
            case PvP -> {
                btShowTable.setVisible(false);
                btNextMove.setVisible(false);
            }
            case PvC -> {
                btShowTable.setVisible(false);
                if (game.getTurn() == Player.PLAYER1) {
                    btNextMove.setDisable(true);
                }
            }
            case CvC -> {
                btShowTable.setVisible(true);
                btNextMove.setVisible(true);
            }
        }

        // Display all the coins on the view
        int[] coins = game.getSettings().getCoins();
        for (int i = 0; i < coins.length; i++) {

            // A coin is clickable(can be collected) is the first(left most) or last(right most) coin
            boolean isCoinClickable = i == 0 || i == coins.length - 1;

            // Creates a new coin instance to add on the Coins Container
            Coin coin = new Coin(coins[i], isCoinClickable && game.getType() != GameType.CvC);

            coinsContainer.getChildren().add(coin);

            // Dim the coin if it's not clickable
            if (!isCoinClickable) coin.setOpacity(0.4);

            // if the game type is PvP or PvC, the coin bust have an event to catch the players actions
            if (game.getType() != GameType.CvC) coin.setOnMouseClicked(event -> handleOnCoinClicked(coin));
        }

        switchBoards();
    }

    @FXML
    void onGoBackClicked() {
        Stage stage = (Stage) coinsContainer.getScene().getWindow();

        // if the game is over then just close peacefully
        if (game.isGameOver()) {
            stage.close();
            return;
        }

        Alert confirmation = Alerter.confirm("The game is not over yet", "Are you sure leave?");

        Optional<ButtonType> response = confirmation.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            SoundPlayer.playSad();
            stage.close();
        }
    }

    @FXML
    void onShowTableClicked() {
        SoundPlayer.playClick();

        // Load the solution table view, and inject the solution and coins to it
        Stage dialog = FXMLUtil.loadDialog(View.SOLUTIONTABLE, (SolutionTableController controller) -> {
            controller.inject(game.getSolution(), game.getSettings().getCoins());
        });

        // if unable to load the view, just return since the loadDialog handles the alert
        if (dialog == null) return;

        dialog.setTitle("Solution Table");
        dialog.show();
    }

    @FXML
    void onNextMoveClicked() {
        // If this button got clicked, then it is visible
        // and its visible in the PvC and CvC game modes
        // in the CvC this button will always stay enabled since there are 2 computers playing
        // but in the PvC mode it should be disabled when the computer turn is up(it's the player turn)
        if (game.getType() == GameType.PvC) {
            btNextMove.setDisable(true);
        }

        Player player = game.getTurn();

        // 0: pick first coin
        // 1: pick last coin
        int pick = game.getBestMoveNow();

        Node pickedCoin;
        if (pick == 0) {
            game.pickFirst();
            pickedCoin = coinsContainer.getChildren().getFirst();
        } else {
            game.pickLast();
            pickedCoin = coinsContainer.getChildren().getLast();
        }

        coinsContainer.getChildren().remove(pickedCoin);

        // Increment and add the coin to the player board
        addCoinToPlayer((Coin) pickedCoin, player);

        if (game.isGameOver()) {
            displayWinner();

            // Update the view
            btShowTable.setVisible(true);
            btNextMove.setDisable(true);
            playerOneBoard.setDisable(false);
            playerTwoBoard.setDisable(false);
            return;
        }

        Node nextCoin;
        if (pick == 0) {
            nextCoin = coinsContainer.getChildren().getFirst();
        } else {
            nextCoin = coinsContainer.getChildren().getLast();
        }

        // Update the next coin view
        nextCoin.setOpacity(1);

        // A coin can only be clicked if the game is not Computer vs Computer
        ((Coin) nextCoin).setClickable(game.getType() != GameType.CvC);

        switchBoards();
    }

    private void handleOnCoinClicked(Coin coin) {
        if (!coin.isClickable()) return;

        Player player = game.getTurn();

        //  In a Player vs Computer Game the [player 1] is considered to be the human player
        //  So if the game is PvC and the turn is not for player 1, then it's not his turn
        if (game.getType() == GameType.PvC && player != Player.PLAYER1) {
            Alerter.warn("It is not your turn yet!").show();
            return;
        }

        boolean pickFirst = coin == coinsContainer.getChildren().getFirst();
        if (pickFirst) {
            game.pickFirst();
        } else {
            game.pickLast();
        }

        coinsContainer.getChildren().remove(coin);

        // Increment and add the coin to the player board
        addCoinToPlayer(coin, player);

        if (game.isGameOver()) {
            displayWinner();

            btShowTable.setVisible(true);
            return;
        }

        Node nextCoin;
        if (pickFirst) {
            nextCoin = coinsContainer.getChildren().getFirst();
        } else {
            nextCoin = coinsContainer.getChildren().getLast();
        }

        // Update the next coin view
        nextCoin.setOpacity(1);

        // A coin can only be clicked if the game is not Computer vs Computer
        ((Coin) nextCoin).setClickable(game.getType() != GameType.CvC);

        // Since the human turn is over we enable the next move button to allow the computer to play
        btNextMove.setDisable(false);

        switchBoards();
    }

    // Increment and add the coin to the player board and updates the Game instance as needed
    private void addCoinToPlayer(Coin coin, Player player) {
        // Gets the target coins label to update
        Label coinsLabel = player == Player.PLAYER1 ? lblPlayerOneCoins : lblPlayerTwoCoins;

        String txtCoins = coinsLabel.getText().equals("N/A") ? "" : coinsLabel.getText();

        if (!txtCoins.isEmpty()) {
            txtCoins += ", ";
        }
        txtCoins += coin.getValue();

        coinsLabel.setText(txtCoins);

        // Get the target gain label to update
        Label gainLabel = player == Player.PLAYER1 ? lblPlayerOneGain : lblPlayerTwoGain;

        // Updates the game player's gain
        game.incrementPlayerGainBy(player, coin.getValue());

        gainLabel.setText(String.valueOf(game.getPlayerGain(player)));
    }

    // Adjust the view of the players board according to which turn
    // By Disabling the board of waiting player
    private void switchBoards() {
        if (game.getTurn() == Player.PLAYER1) {
            playerOneBoard.setDisable(false);
            playerTwoBoard.setDisable(true);
        } else {
            playerOneBoard.setDisable(true);
            playerTwoBoard.setDisable(false);
        }
    }

    // Displays the winner
    private void displayWinner() {
        Player winner = game.getWinner();
        String winnerName;
        boolean isDraw;

        // When winner is null then it's a draw
        if (winner == null) {
            winnerName = "";
            isDraw = true;
        } else if (winner == Player.PLAYER1) {
            isDraw = false;
            winnerName = lblPlayerOneName.getText();
        } else {
            isDraw = false;
            winnerName = lblPlayerTwoName.getText();
        }

        try {
            VBox winnerPane = FXMLUtil.load(View.GAMEOVER, (GameOverController controller) -> controller.inject(winnerName, isDraw));
            coinsContainer.getChildren().add(winnerPane);
        } catch (IOException ex) {
            String msg;
            if (isDraw) {
                msg = "It's a draw";
            } else {
                msg = winnerName + " WON the game!";
            }

            Alerter.info("GAME IS OVER", msg).show();
        }
    }
}
