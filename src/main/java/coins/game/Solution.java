package coins.game;

// This solution class contains all the algorithms related to solving the optimal strategy game
public class Solution {

    // Solve the problem given the input coins and returning the generated table
    public static int[][] solve(int[] coins) {
        int n = coins.length;

        if (n % 2 != 0) {
            System.out.println("The number of coins should be an even number!");
            return null;
        }

        int[][] table = new int[n][n];

        for (int i = 0; i < n; i++) {
            // the main diagonal, the initial values (a single coin)
            table[i][i] = coins[i];

            // the second diagonal, the initial values (two coins)
            if (i < n - 1)
                table[i][i+1] = Math.max(coins[i], coins[i+1]);
        }

        // Iterate the table diagonally
        for (int col = 2; col < n; col++) {
            for (int i = 0, j = col; i < n && j < n; i++, j++) {
                // take the maximum of either choice
                table[i][j] = Math.max(
                        // since the other player is playing optimally after chosen the left most coin
                        // he will leave me the minimum he can
                        coins[i] + Math.min(table[i + 1][j - 1], table[i + 2][j]),
                        // since the other player is playing optimally after chosen the right most coin
                        // he will leave me the minimum he can
                        coins[j] + Math.min(table[i + 1][j - 1], table[i][j - 2])
                );
            }
        }

        return table;
    }

    // Use the generated table to trace back the order in which the coins where taken by the first player
    // [int[] coins] being the input coins
    public static int[] coinsTrace(int[] coins, int[][] table) {
        int n = coins.length;

        if (n % 2 != 0) {
            System.out.println("The number of coins should be an even number!");
            return null;
        }

        int[] result = new int[n / 2];

        coinsTraceHelper(0, n - 1, coins, table, result, 0);

        return result;
    }

    private static void coinsTraceHelper(int i, int j, int[] coins, int[][] table, int[] res, int next) {
        // Stop condition: joined unfilled diagonal
        if (i > j) return;

        // Stop condition: reached the end of the game
        if (i == j) {
            res[next] = coins[i];
            return;
        }

        // Stop condition: second diagonal reached
        if (j - i == 1) {
            res[next] = Math.max(table[i][j - 1], table[i + 1][j]);
            return;
        }

        // if chosen left most coin what is the gain
        int takenLeft = coins[i];
        // since the other player is playing optimally after chosen the left most coin
        // he will leave me the minimum he can
        takenLeft += Math.min(table[i + 1][j - 1], table[i + 2][j]);

        // if chosen right most coin what is the gain
        int takenRight = coins[j];
        // since the other player is playing optimally after chosen the right most coin
        // he will leave me the minimum he can
        takenRight += Math.min(table[i][j - 2], table[i + 1][j - 1]);

        if (takenRight > takenLeft) {
            res[next++] = coins[j];

            if (table[i][j - 2] < table[i + 1][j - 1]) {
                coinsTraceHelper(i, j - 2, coins, table, res, next);
            } else {
                coinsTraceHelper(i + 1, j - 1, coins, table, res, next);
            }
        } else if (takenLeft > takenRight) {
            res[next++] = coins[i];

            if (table[i + 1][j - 1] < table[i + 2][j]) {
                coinsTraceHelper(i + 1, j - 1, coins, table, res, next);
            } else {
                coinsTraceHelper(i + 2, j, coins, table, res, next);
            }
        } else {
            if (coins[i] > coins[j]) {
                res[next++] = coins[i];
                if (table[i][j - 2] < table[i + 1][j - 1]) {
                    coinsTraceHelper(i, j - 2, coins, table, res, next);
                } else {
                    coinsTraceHelper(i + 1, j - 1, coins, table, res, next);
                }
            } else {
                res[next++] = coins[j];

                if (table[i][j - 2] < table[i + 1][j - 1]) {
                    coinsTraceHelper(i, j - 2, coins, table, res, next);
                } else {
                    coinsTraceHelper(i + 1, j - 1, coins, table, res, next);
                }
            }
        }
    }

    // returns the best move at (ith, jth) position
    // 0: pick left most coin
    // 1: pick right most coin
    public static int getBestMoveAt(int i, int j, int[] coins, int[][] table) {
        if (i == j) {
            return 0;
        }

        if (j - i == 1) {
            if (table[i][j - 1] > table[i + 1][j]) {
                return 0;
            } else {
                return 1;
            }
        }

        // if chosen left most coin what is the gain
        int takenLeft = coins[i];
        // since the other player is playing optimally after chosen the left most coin
        // he will leave me the minimum he can
        takenLeft += Math.min(table[i + 1][j - 1], table[i + 2][j]);

        // if chosen right most coin what is the gain
        int takenRight = coins[j];
        // since the other player is playing optimally after chosen the right most coin
        // he will leave me the minimum he can
        takenRight += Math.min(table[i][j - 2], table[i + 1][j - 1]);

        if (takenLeft > takenRight) {
            return 0;
        } else if (takenRight > takenLeft) {
            return 1;
        } else {
            if (coins[i] >= coins[j]) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
