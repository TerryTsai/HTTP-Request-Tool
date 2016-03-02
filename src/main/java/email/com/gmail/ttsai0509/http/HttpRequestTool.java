package email.com.gmail.ttsai0509.http;

import email.com.gmail.ttsai0509.http.controller.MainController;
import email.com.gmail.ttsai0509.http.controller.RequestController;
import email.com.gmail.ttsai0509.http.controller.ResponseController;
import email.com.gmail.ttsai0509.http.utils.FXMLUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import org.w3c.tidy.Tidy;

public class HttpRequestTool extends Application {

    public static void main(String[] args) {
        Application.launch(HttpRequestTool.class);
    }

    // TODO : Ideally, these would be dependency injected.
    public static Tidy tidy;
    public static OkHttpClient client;
    public static MainController mainController;
    public static RequestController requestController;
    public static ResponseController responseController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setIndentContent(true);

        client = new OkHttpClient();
        requestController = FXMLUtils.loadAndGetCtrl(getClass().getResource("/request.fxml"));
        responseController = FXMLUtils.loadAndGetCtrl(getClass().getResource("/response.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent parent = loader.load();
        mainController = loader.getController();

        Scene scene = new Scene(parent, 1024, 576);
        primaryStage.setTitle("HTTP Request Tool");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();

    }

}
