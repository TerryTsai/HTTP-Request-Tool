package email.com.gmail.ttsai0509.http.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public interface AppController<A extends Application> {

    public void initialize(A app);

    public static <A extends Application, C extends AppController<A>> C loadAndGetCtrl(URL resource, A app) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            loader.load();
            C ctrl = loader.getController();
            ctrl.initialize(app);
            return ctrl;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <A extends Application, C extends AppController<A>> FXMLLoader loadAndGetLoader(URL resource, A app) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            loader.load();
            C ctrl = loader.getController();
            ctrl.initialize(app);
            return loader;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T, A extends Application, C extends AppController<A>> T loadAndGetRoot(URL resource, A app) {
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            T root = loader.load();
            C ctrl = loader.getController();
            ctrl.initialize(app);
            return root;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
