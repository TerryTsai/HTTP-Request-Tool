package email.com.gmail.ttsai0509.http;

import email.com.gmail.ttsai0509.http.controller.MainController;
import email.com.gmail.ttsai0509.http.controller.RequestController;
import email.com.gmail.ttsai0509.http.controller.ResponseController;
import email.com.gmail.ttsai0509.http.utils.AppController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import org.w3c.tidy.Tidy;

public class HttpRequestTool extends Application {

    public static void main(String[] args) {
        Application.launch(HttpRequestTool.class);
    }

    private Tidy tidy;
    private OkHttpClient http;
    private MainController mainController;
    private RequestController requestController;
    private ResponseController responseController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setIndentContent(true);

        http = new OkHttpClient();

        requestController = AppController.loadAndGetCtrl(getClass().getResource("/request.fxml"), this);
        responseController = AppController.loadAndGetCtrl(getClass().getResource("/response.fxml"), this);
        mainController = AppController.loadAndGetCtrl(getClass().getResource("/main.fxml"), this);

        Scene scene = new Scene(mainController.root, 1024, 576);
        primaryStage.setTitle("HTTP Request Tool");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();

    }

    public Tidy getTidy() {
        return tidy;
    }

    public OkHttpClient getHttp() {
        return http;
    }

    public MainController getMainCtrl() {
        return mainController;
    }

    public RequestController getRequestCtrl() {
        return requestController;
    }

    public ResponseController getResponseCtrl() {
        return responseController;
    }

}
