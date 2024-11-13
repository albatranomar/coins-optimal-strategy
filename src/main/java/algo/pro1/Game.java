package algo.pro1;

import algo.pro1.util.SoundPlayer;

import java.util.Random;

// Encapsulate the Game Logic and methods of the game flow
public class Game {
    private final GameType type;

    // The settings of this game
    private final GameSettings settings;

    // Game turn indicate the turn of the players
    private Player turn;

    private int playerOneGain, playerTwoGain;

    private GameStatus status = GameStatus.NOT_STARTED;

    // The indexes of the pickable coins
    private int firstCoin, lastCoin;

    private final int[][] solution;
    public Game(GameType type, GameSettings settings) {
        this.type = type;
        this.settings = settings;
        this.turn = Player.PLAYER1;

        int n = settings.getCoins().length;
        firstCoin = 0;
        lastCoin = n - 1;

        if (settings.getEntryType() == GameSettings.RANDOMIZE) {
            int[] randomized = new int[n];
            for (int i = 0; i < n; i++) {
                randomized[i] = new Random().nextInt(99) + 1;
            }

            settings.setCoins(randomized);
        }

        solution = Solution.solve(settings.getCoins());
    }


    // returns the best move at any given position
    // 0: pick first coin
    // 1: pick last coin
    public int getBestMoveNow() {
        int i = firstCoin;
        int j = lastCoin;
        if (i == j) {
            return 0;
        }

        if (j - i == 1) {
            if (solution[i][j - 1] > solution[i + 1][j]) {
                return 0;
            } else {
                return 1;
            }
        }

        // if chosen left most coin what is the gain
        int takenLeft = settings.getCoins()[i];
        // since the other player is playing optimally after chosen the left most coin
        // he will leave me the minimum he can
        takenLeft += Math.min(solution[i + 1][j - 1], solution[i + 2][j]);

        // if chosen right most coin what is the gain
        int takenRight = settings.getCoins()[j];
        // since the other player is playing optimally after chosen the right most coin
        // he will leave me the minimum he can
        takenRight += Math.min(solution[i][j - 2], solution[i + 1][j - 1]);

        if (takenLeft > takenRight) {
            return 0;
        } else {
            return 1;
        }
    }

    // starts the game
    public void start() {
        status = GameStatus.IN_PROGRESS;
        SoundPlayer.playStart();
    }

    // Returns the gain of a given player
    public int getPlayerGain(Player player) {
        return player == Player.PLAYER1 ? playerOneGain : playerTwoGain;
    }

    // Increments the gain of a given player by a given amount
    public void incrementPlayerGainBy(Player player, int amount) {
        SoundPlayer.playCoin();
        if (player == Player.PLAYER1) {
            playerOneGain += amount;
        } else {
            playerTwoGain += amount;
        }
    }

    // Returns the winner of the game
    // return null if draw
    public Player getWinner() {
        SoundPlayer.playGameOver();
        if (playerOneGain > playerTwoGain) {
            return Player.PLAYER1;
        } else if (playerTwoGain > playerOneGain) {
            return Player.PLAYER2;
        } else {
            return null;
        }
    }

    public GameType getType() {
        return type;
    }
    public GameSettings getSettings() {
        return settings;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        if (turn == null) {
            throw new IllegalArgumentException("The player turn can't be null");
        }
        this.turn = turn;
    }

    public boolean isGameOver() {
        return status == GameStatus.GAME_OVER;
    }

    public int[][] getSolution() {
        return solution;
    }


    // Handel the picking of the first coin & switch the player turn
    public void pickFirst() {
        if (status != GameStatus.IN_PROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        // switch the player turn
        turn = (turn == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;

        if (firstCoin + 1 > lastCoin) {
            status = GameStatus.GAME_OVER;
        }

        firstCoin++;
    }

    // Handel the picking of the last coin & switch the player turn
    public void pickLast() {
        if (status != GameStatus.IN_PROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        // switch the player turn
        turn = (turn == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;

        if (lastCoin - 1 < 0) {
            status = GameStatus.GAME_OVER;
        }

        lastCoin--;
    }

    // Game type is one of:
    // Player vs Player
    // Player vs Computer
    // Simulate an Optimal Game(Computer vs Computer)
    public enum GameType { PvP, PvC, CvC }

    public enum GameStatus { NOT_STARTED, IN_PROGRESS, GAME_OVER }

    public enum Player { PLAYER1, PLAYER2;}
}
