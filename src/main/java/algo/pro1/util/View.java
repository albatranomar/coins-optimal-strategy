package algo.pro1.util;

public enum View {
    LANDING("views/MainPane.fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
