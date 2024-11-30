package coins.game;

// This class holds the player preferences and settings related to the game play
public class GameSettings {
    // Entry Type is how the game will get the coins
    // Randomize
    // Manually
    // File Loaded
    private EntryType entryType;

    private final int[] range = new int[] {1, 200};

    // If not randomize will save the coins
    private int[] coins;

    public GameSettings() {
        // Defaults to a randomize entry and 4 coins
        this(EntryType.RANDOM, new int[4]);
    }

    public GameSettings(EntryType entryType, int[] coins) {
        this.entryType = entryType;
        this.coins = coins;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        if (entryType == null) {
            throw new IllegalArgumentException("The Entry Type can't be null");
        }

        this.entryType = entryType;
    }

    public int[] getCoins() {
        return coins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }

    public int getMax() {
        return range[1];
    }

    public int getMin() {
        return range[0];
    }

    public void setMax(int max) {
        range[1] = max;
    }

    public void setMin(int min) {
        range[0] = min;
    }

    public enum EntryType { RANDOM, MANUAL, FILE }
}
