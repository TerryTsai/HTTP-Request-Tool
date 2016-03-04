package email.com.gmail.ttsai0509.http;

import email.com.gmail.ttsai0509.http.controller.FinderController;
import email.com.gmail.ttsai0509.http.controller.MainController;
import email.com.gmail.ttsai0509.http.controller.RequestController;
import email.com.gmail.ttsai0509.http.controller.ResponseController;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import org.w3c.tidy.Tidy;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class HttpRequestTool extends Application {

    public static void main(String[] args) {
        Application.launch(HttpRequestTool.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        initDependencies();

        Scene scene = new Scene(mainController.root, 1024, 576);
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().equals(KeyCode.N))
                newRequest();
        });

        primaryStage.setTitle("HTTP Request Tool");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();

    }

    /******************************************************************
     *                                                                *
     * Dependencies
     *                                                                *
     ******************************************************************/

    private Tidy tidy;
    private OkHttpClient http;
    private ListProperty<RequestConfig> history;
    private ScriptEngine jsEngine;
    private MainController mainController;
    private RequestController requestController;
    private ResponseController responseController;
    private FinderController finderController;

    private void initDependencies() {
        tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setIndentContent(true);
        tidy.setShowWarnings(false);
        tidy.setQuiet(true);

        http = new OkHttpClient();

        history = new SimpleListProperty<>(FXCollections.observableArrayList());

        jsEngine = new ScriptEngineManager().getEngineByName("js");

        requestController = quickCtrl("/fxml/request.fxml");
        responseController = quickCtrl("/fxml/response.fxml");
        finderController = quickCtrl("/fxml/finder.fxml");
        mainController = quickCtrl("/fxml/main.fxml");
    }

    public Tidy getTidy() {
        return tidy;
    }

    public OkHttpClient getHttp() {
        return http;
    }

    public ListProperty<RequestConfig> getHistory() {
        return history;
    }

    public ScriptEngine getJsEngine() {
        return jsEngine;
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

    public FinderController getFinderCtrl() {
        return finderController;
    }

    /******************************************************************
     *                                                                *
     * Convenience (Move to service when this gets too large)
     *                                                                *
     ******************************************************************/

    public <C extends AppCtrl<HttpRequestTool>> C quickCtrl(String resource) {
        return AppCtrl.loadGetCtrl(getClass().getResource(resource), this);
    }

    public <T> T quickRoot(String resource) {
        return AppCtrl.loadGetRoot(getClass().getResource(resource), this);
    }

    public void newRequest() {
        Platform.runLater(() -> requestController.bindRequest(new RequestConfig()));
    }

}
