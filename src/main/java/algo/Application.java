package algo;

import algo.util.FXMLUtil;
import algo.util.View;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = FXMLUtil.load(View.LANDING);
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setTitle("Omar Albatran - 1221344");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}