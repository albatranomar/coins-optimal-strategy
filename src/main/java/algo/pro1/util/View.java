package algo.pro1.util;

public enum View {
    MAIN("views/MainPane.fxml"),
    SETTINGS("views/SettingsPane.fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
