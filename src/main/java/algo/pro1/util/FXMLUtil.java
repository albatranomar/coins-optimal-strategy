package algo.pro1.util;

import algo.pro1.Application;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class FXMLUtil {
    public static <T> T load(View view) throws IOException {
        return load(view, controller -> {});
    }

    public static <T> T load(View view, ControllerLambda<T> callback) throws IOException {
        URL fxmlUrl = Application.class.getResource(view.getPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + view.getPath());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        System.out.println("Loaded FXML: " + fxmlUrl);

        T loaded = fxmlLoader.load();
        callback.process(fxmlLoader.getController());

        return loaded;
    }

    public interface ControllerLambda<T> {
        void process(T controller);
    }
}
