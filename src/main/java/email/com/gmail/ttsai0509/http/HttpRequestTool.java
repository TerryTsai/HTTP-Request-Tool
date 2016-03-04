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
import javafx.scene.Scene;
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

        requestController = AppCtrl.loadGetCtrl(getClass().getResource("/fxml/request.fxml"), this);
        responseController = AppCtrl.loadGetCtrl(getClass().getResource("/fxml/response.fxml"), this);
        finderController = AppCtrl.loadGetCtrl(getClass().getResource("/fxml/finder.fxml"), this);
        mainController = AppCtrl.loadGetCtrl(getClass().getResource("/fxml/main.fxml"), this);
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

}
