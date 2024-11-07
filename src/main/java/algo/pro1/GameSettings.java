package algo.pro1;

// This class holds the player preferences and settings related to the game play
public class GameSettings {
    // Entry Type is how the game will get the coins
    // 0: Randomize
    // 1: Manually
    // 2: File Loaded
    private int entryType;

    public static final int RANDOMIZE = 0;
    public static final int MANUALLY = 1;
    public static final int FILELOADED = 2;

    // If not randomize will save the coins
    private int[] coins;

    public GameSettings() {
        this(0, new int[4]);
    }

    public GameSettings(int entryType, int[] coins) {
        this.entryType = entryType;
        this.coins = coins;
    }

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public int[] getCoins() {
        return coins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }
}
