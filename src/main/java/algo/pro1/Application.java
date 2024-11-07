package algo.pro1;

import algo.pro1.util.FXMLUtil;
import algo.pro1.util.View;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

// This is the main entry class the first scene is loaded and displayed here
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the first scene of the application
        BorderPane borderPane = FXMLUtil.load(View.MAIN);
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setTitle("Optimal Strategy Game - Omar Albatran 1221344");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}