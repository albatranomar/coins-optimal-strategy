package algo.pro1.util;

import algo.pro1.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FXMLUtil {
    public static <P> P load(View view) throws IOException {
        return load(view, controller -> {});
    }

    public static <P, C> P load(View view, ControllerLambda<C> callback) throws IOException {
        URL fxmlUrl = Application.class.getResource(view.getPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + view.getPath());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        System.out.println("Loaded FXML: " + fxmlUrl);

        P loaded = fxmlLoader.load();
        callback.process(fxmlLoader.getController());

        return loaded;
    }

    public static <T> Stage loadDialog(View view, ControllerLambda<T> callback) throws IOException {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Scene dialogScene = new Scene(load(view, callback));
        dialog.setScene(dialogScene);

        return dialog;
    }

    public interface ControllerLambda<T> {
        void process(T controller);
    }
}
