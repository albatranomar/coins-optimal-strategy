package algo.util;

import algo.Application;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class FXMLUtil {

    public static <T> T load(View view) {
        return load(view, controller -> {});
    }

    public static <T> T load(View view, ControllerLambda<T> callback) {
        URL fxmlUrl = Application.class.getResource(view.getPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + view.getPath());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        try {
            System.out.println("Loaded FXML: " + fxmlUrl);
            T loaded = fxmlLoader.load();
            callback.process(fxmlLoader.getController());
            return loaded;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface ControllerLambda<T> {
        void process(T controller);
    }
}
