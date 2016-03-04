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
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.w3c.tidy.Tidy;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;

public class HttpRequestTool extends Application {

    public static void main(String[] args) {
        Application.launch(HttpRequestTool.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        initDependencies();

        Scene scene = new Scene(mainController.root, 1024, 576);
        scene.setOnKeyPressed(shortcutHandler);

        primaryStage.setTitle("HTTP Request Tool");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> quitApp());

        primaryStage.show();

    }

    /******************************************************************
     *                                                                *
     * Dependencies
     *                                                                *
     ******************************************************************/

    private Tidy tidy;
    private OkHttpClient http;
    private ScriptEngine jsEngine;
    private ListProperty<RequestConfig> history;
    private MapProperty<String, ListProperty<RequestConfig>> collections;

    // TODO : Move Request and Response here, instead of their controllers.

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

        jsEngine = new ScriptEngineManager().getEngineByName("js");

        history = new SimpleListProperty<>(FXCollections.observableArrayList());

        collections = new SimpleMapProperty<>(FXCollections.observableHashMap());

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

    public ScriptEngine getJsEngine() {
        return jsEngine;
    }

    public ListProperty<RequestConfig> getHistory() {
        return history;
    }

    public MapProperty<String, ListProperty<RequestConfig>> getCollections() {
        return collections;
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
     * Shortcuts
     *                                                                *
     ******************************************************************/

    private EventHandler<KeyEvent> shortcutHandler = event -> {
        if (event.isControlDown()) {
            switch (event.getCode()) {
                case N: newRequest(); break;
                case ENTER: sendRequest(); break;
                case U: requestController.expandUrl(); break;
                case H: requestController.expandHeaders(); break;
                case B: requestController.expandBody(); break;
                case S: requestController.expandScript(); break;
            }
        }
    };

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

    public void quitApp() {
        Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void newRequest() {
        Platform.runLater(() -> requestController.bindRequest(new RequestConfig()));
    }

    public void sendRequest() {
        RequestConfig config = requestController.getRequest();
        history.add(config.copy());
        http.newCall(config.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Platform.runLater(() -> responseController.setResult(config, response));
            }
        });
    }

}
