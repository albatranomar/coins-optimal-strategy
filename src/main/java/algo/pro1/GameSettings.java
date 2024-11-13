package algo.pro1;

// This class holds the player preferences and settings related to the game play
public class GameSettings {
    // Entry Type is how the game will get the coins
    // Randomize
    // Manually
    // File Loaded
    private EntryType entryType;

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

    public enum EntryType { RANDOM, MANUAL, FILE }
}
