package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class MainController implements AppCtrl<HttpRequestTool> {

    @FXML public BorderPane root;
    @FXML public MenuItem miExit;
    @FXML public ListView<RequestConfig> lvHistory;
    @FXML public MenuItem miNew;
    @FXML public MenuItem miSubmit;
    @FXML public StackPane responseContainer;
    @FXML public StackPane requestContainer;

    @Override
    public void postLoad(HttpRequestTool app) {
        miExit.setOnAction(event -> Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        }));

        lvHistory.setPlaceholder(new Label("Makes some requests."));
        lvHistory.setCellFactory(param -> AppCtrl.loadGetCtrl(getClass().getResource("/history-cell.fxml"), app));
        lvHistory.setItems(app.getHistory());

        responseContainer.getChildren().setAll(app.getResponseCtrl().root);
        requestContainer.getChildren().setAll(app.getRequestCtrl().root);

        miNew.setOnAction(event -> app.getRequestCtrl().bindRequest(new RequestConfig()));

        miSubmit.setOnAction(event -> {
            RequestConfig config = app.getRequestCtrl().getRequest();
            app.getHistory().add(config.copy());
            app.getHttp().newCall(config.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Platform.runLater(() -> app.getResponseCtrl().setResult(config, response));
                }
            });
        });
    }

}
