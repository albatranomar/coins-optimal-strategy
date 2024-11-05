package algo;

import algo.solution.Solution;
import algo.util.FXMLUtil;
import algo.util.View;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

// This is the main entry class the first scene is loaded and displayed here
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the first scene of the application
        BorderPane borderPane = FXMLUtil.load(View.LANDING);
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setTitle("Omar Albatran - 1221344");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        var c = new int[]{8, 15, 3, 7, 6, 5};
        var t = Solution.solve(c);
        var r = Solution.coinsTrace(c, t);

        launch();
    }
}