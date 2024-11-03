package algo.util;

public enum View {
    LANDING("views/OptimalStrategyGame .fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
