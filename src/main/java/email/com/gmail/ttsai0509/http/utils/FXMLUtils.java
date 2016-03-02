package email.com.gmail.ttsai0509.http.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Terry on 3/1/2016.
 */
public class FXMLUtils {

    private FXMLUtils() {}

    public static class FXMLUtilsException extends RuntimeException {
        public FXMLUtilsException(String message) {
            super(message);
        }
    }

    public static <T> T loadAndGetCtrl(URL resource) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new FXMLUtilsException(e.getMessage());
        }
    }

    public static FXMLLoader loadAndGetLoader(URL resource) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            loader.load();
            return loader;
        } catch (IOException e) {
            throw new FXMLUtilsException(e.getMessage());
        }
    }

    public static <T> T loadAndGetRoot(URL resource) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            return loader.load();
        } catch (IOException e) {
            throw new FXMLUtilsException(e.getMessage());
        }
    }

}
