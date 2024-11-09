package algo.pro1.util;

import algo.pro1.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FXMLUtil {
    // Load a View with no controller callback
    public static <P> P load(View view) throws IOException {
        return load(view, controller -> {});
    }

    // Load a View with specified controller callback
    public static <P, C> P load(View view, ControllerLambda<C> callback) throws IOException {
        // Gets the resource URL/Path
        URL fxmlUrl = App.class.getResource(view.getPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + view.getPath());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        System.out.println("Loaded FXML: " + fxmlUrl);

        P loaded = fxmlLoader.load();
        callback.process(fxmlLoader.getController());

        return loaded;
    }

    // Creates a Dialog for loading specified View
    // Returns null if failed to load
    public static <T> Stage loadDialog(View view, ControllerLambda<T> callback) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        try {
            Scene dialogScene = new Scene(load(view, callback));
            dialog.setScene(dialogScene);
        } catch (IOException e) {
            Alerter.error("View Not Found", "Unable to load [" + view.getPath() + "]!").show();
            return null;
        }

        return dialog;
    }

    // An interface for the Controller Callback function
    public interface ControllerLambda<T> {
        void process(T controller);
    }
}
