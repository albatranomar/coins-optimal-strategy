package algo.pro1;

// This class holds the player preferences and settings related to the game play
public class GameSettings {
    private boolean coinRandomize;
    private int[] coins;

    public GameSettings() {
        this(true, null);
    }

    public GameSettings(boolean coinRandomize, int[] coins) {
        this.coinRandomize = coinRandomize;
        this.coins = coins;
    }

    public boolean isCoinRandomize() {
        return coinRandomize;
    }

    public void setCoinRandomize(boolean coinRandomize) {
        this.coinRandomize = coinRandomize;
    }

    public int[] getCoins() {
        return coins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }
}
