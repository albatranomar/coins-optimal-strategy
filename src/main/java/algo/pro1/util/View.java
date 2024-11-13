package algo.pro1.util;

public enum View {
    MAIN("views/MainView.fxml"),
    SETTINGS("views/SettingsView.fxml"),
    PLAYGROUND("views/PlaygroundView.fxml"),
    SOLUTIONTABLE("views/SolutionTableView.fxml"),
    GAMEOVER("views/GameOverView.fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
