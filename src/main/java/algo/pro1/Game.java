package algo.pro1;

import java.util.Random;

// Encapsulate the Game Logic and methods of the game flow
public class Game {
    // Game type is one of:
    // 0: Player vs Player
    // 1: Player vs Computer
    // 2: Simulate an Optimal Game(Computer vs Computer)
    private int type;

    public static final int PvP = 0;
    public static final int PvC = 1;
    public static final int CvC = 2;

    // The settings of this game
    private final GameSettings settings;

    // Game turn indicate the turn of the players
    // 1: player 1
    // 2: player 2
    private int turn;

    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    private int playerOneGain;
    private int playerTwoGain;

    // Game status:
    // 0: not started
    // 1: in-progress
    // 2: game-over
    private int status;

    public static final int NOTSTARTED = 0;
    public static final int INPROGRESS = 1;
    public static final int GAMEOVER = 2;

    // The indexes of the pickable coins
    private int firstCoin, lastCoin;

    private final int[][] solution;

    public Game(int type, GameSettings settings) {
        this.type = type;
        this.settings = settings;
        this.turn = PLAYER1;

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
        status = INPROGRESS;
    }

    // Returns the gain of a given player
    public int getPlayerGain(int player) {
        return player == PLAYER1 ? playerOneGain : playerTwoGain;
    }

    // Increments the gain of a given player by a given amount
    public void incrementPlayerGainBy(int player, int amount) {
        if (player == PLAYER1) {
            playerOneGain += amount;
        } else {
            playerTwoGain += amount;
        }
    }

    // Returns the winner of the game
    // 0: draw
    // 1: player 1
    // 2: player 2
    public int getWinner() {
        if (playerOneGain > playerTwoGain) {
            return PLAYER1;
        } else if (playerTwoGain > playerOneGain) {
            return PLAYER2;
        } else {
            return 0;
        }
    }

    public int getType() {
        return type;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isGameOver() {
        return status == GAMEOVER;
    }

    public int[][] getSolution() {
        return solution;
    }

    // Handel the picking of the first coin & switch the player turn
    public void pickFirst() {
        if (status != INPROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        // switch the player turn
        turn = (turn == PLAYER1) ? PLAYER2 : PLAYER1;

        if (firstCoin + 1 > lastCoin) {
            status = GAMEOVER;
        }

        firstCoin++;
    }

    // Handel the picking of the last coin & switch the player turn
    public void pickLast() {
        if (status != INPROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        // switch the player turn
        turn = (turn == PLAYER1) ? PLAYER2 : PLAYER1;

        if (lastCoin - 1 < 0) {
            status = GAMEOVER;
        }

        lastCoin--;
    }
}
