package algo.pro1;

import java.util.Random;

public class Game {
    // Game type is one of:
    // 0: Player vs Player
    // 1: Simulate an Optimal Game(Computer vs Computer)
    private int type;

    public static final int PvP = 0;
    public static final int CvC = 1;

    // The settings of this game
    private GameSettings settings;

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

    // The indexes of the available coins to pick from
    private int firstCoin, lastCoin;

    private int[][] solution;

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

    public void start() {
        status = INPROGRESS;
    }

    public int getPlayerGain(int player) {
        return player == PLAYER1 ? playerOneGain : playerTwoGain;
    }

    public void incrementPlayerGainBy(int player, int amount) {
        if (player == PLAYER1) {
            playerOneGain += amount;
        } else {
            playerTwoGain += amount;
        }
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFirstCoin() {
        return firstCoin;
    }

    public int getLastCoin() {
        return lastCoin;
    }

    public int[][] getSolution() {
        return solution;
    }

    public int pickFirst() {
        if (status != INPROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        turn = (turn == PLAYER1) ? PLAYER2 : PLAYER1;

        if (firstCoin + 1 > lastCoin) {
            status = GAMEOVER;
        }

        return firstCoin++;
    }

    public int pickLast() {
        if (status != INPROGRESS) throw new RuntimeException("Can't pick a coin if the game is not in progress");

        turn = (turn == PLAYER1) ? PLAYER2 : PLAYER1;

        if (lastCoin - 1 < 0) {
            status = GAMEOVER;
        }

        return lastCoin--;
    }
}
