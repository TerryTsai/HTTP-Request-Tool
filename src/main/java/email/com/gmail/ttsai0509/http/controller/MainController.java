package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.FXMLUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class MainController {

    @FXML public MenuItem miExit;
    @FXML public ListView<RequestConfig> lvHistory;
    @FXML public MenuItem miNew;
    @FXML public MenuItem miSubmit;
    @FXML public StackPane responseContainer;
    @FXML public StackPane requestContainer;

    @FXML
    public void initialize() {
        miExit.setOnAction(event -> Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        }));

        lvHistory.setPlaceholder(new Label("Makes some requests."));
        lvHistory.setCellFactory(param -> FXMLUtils.loadAndGetCtrl(getClass().getResource("/history-cell.fxml")));
        lvHistory.setItems(FXCollections.observableArrayList());

        responseContainer.getChildren().setAll(HttpRequestTool.responseController.root);
        requestContainer.getChildren().setAll(HttpRequestTool.requestController.root);

        miNew.setOnAction(event -> HttpRequestTool.requestController.bindRequest(new RequestConfig()));

        miSubmit.setOnAction(event -> {
            lvHistory.getItems().add(HttpRequestTool.requestController.getRequest().copy());
            HttpRequestTool.client.newCall(HttpRequestTool.requestController.getRequest().build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Platform.runLater(() -> HttpRequestTool.responseController.setResponse(response));
                }
            });
        });
    }
}
