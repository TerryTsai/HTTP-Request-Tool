package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.util.stream.Collectors;

public class RequestController implements AppCtrl<HttpRequestTool> {

    @FXML public Accordion root;
    @FXML public TextField tfUrl;
    @FXML public ComboBox<String> cbMethod;
    @FXML public TextField tfKey;
    @FXML public TextField tfValue;
    @FXML public ListView<Pair<String, String>> lvHeaders;
    @FXML public Button btnHeaderAdd;
    @FXML public TextField tfType;
    @FXML public TextArea taBody;
    @FXML public TextArea taScript;

    @Override
    public void postLoad(HttpRequestTool app) {
        cbMethod.setItems(FXCollections.observableArrayList("GET", "POST", "PUT", "DELETE"));

        lvHeaders.setPlaceholder(new Label("Nothing to see here."));
        lvHeaders.setCellFactory(param -> app.quickCtrl("/fxml/cell/header-cell.fxml"));
        lvHeaders.setItems(FXCollections.observableArrayList());

        btnHeaderAdd.setOnAction(event -> {
            boolean found = lvHeaders.getItems()
                    .stream()
                    .anyMatch(p -> p.getKey().equalsIgnoreCase(tfKey.getText()));

            if (found)
                new Alert(Alert.AlertType.ERROR, tfKey.getText() + " already added.", ButtonType.OK).showAndWait();
            else
                lvHeaders.getItems().add(new Pair<>(tfKey.getText(), tfValue.getText()));

            tfKey.clear();
            tfValue.clear();
        });


        bindRequest(new RequestConfig());
    }

    /******************************************************************
     *                                                                *
     * Convenience
     *                                                                *
     ******************************************************************/

    // TODO : RequestFocus fails (animation?). Have to run twice to focus.

    public void expandUrl() {
        Platform.runLater(() -> {
            root.setExpandedPane(root.getPanes().get(0));
            tfUrl.requestFocus();
        });
    }

    public void expandHeaders() {
        Platform.runLater(() -> {
            root.setExpandedPane(root.getPanes().get(1));
            tfKey.requestFocus();
        });
    }

    public void expandBody() {
        Platform.runLater(() -> {
            root.setExpandedPane(root.getPanes().get(2));
            tfType.requestFocus();
        });
    }

    public void expandScript() {
        Platform.runLater(() -> {
            root.setExpandedPane(root.getPanes().get(3));
            taScript.requestFocus();
        });
    }

    /* RequestConfig Binding */

    // TODO : Move this out of controller and wrap into observable.
    private RequestConfig request;

    public RequestConfig getRequest() {
        return request;
    }

    public void bindRequest(RequestConfig request) {

        if (this.request != null) {
            // Unbind previous RequestConfig from controls
            this.request.urlProperty().unbind();
            this.request.bodyProperty().unbind();
            this.request.mediaProperty().unbind();
            this.request.methodProperty().unbind();
            this.request.scriptProperty().unbind();
            lvHeaders.getItems().removeListener(this.request.headerListener);

            // Clear residual RequestConfig data
            tfUrl.setText("");
            taBody.setText("");
            tfType.setText("");
            cbMethod.getSelectionModel().clearSelection();
            taScript.setText("");
            lvHeaders.getItems().clear();
        }

        this.request = request;

        if (this.request != null) {
            // Update controls with new RequestConfig data
            tfUrl.setText(this.request.getUrl());
            taBody.setText(this.request.getBody());
            tfType.setText(this.request.getMedia());
            cbMethod.getSelectionModel().select(this.request.getMethod());
            taScript.setText(this.request.getScript());
            lvHeaders.getItems().setAll(this.request.getHeaders().entrySet()
                            .stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toList())
            );

            // Bind RequestConfig to controls for changes
            this.request.urlProperty().bind(tfUrl.textProperty());
            this.request.bodyProperty().bind(taBody.textProperty());
            this.request.mediaProperty().bind(tfType.textProperty());
            this.request.methodProperty().bind(cbMethod.getSelectionModel().selectedItemProperty());
            this.request.scriptProperty().bind(taScript.textProperty());
            lvHeaders.getItems().addListener(this.request.headerListener);
        }

        // Default to GET request if no method selected
        String method = cbMethod.getSelectionModel().getSelectedItem();
        if (method == null || method.isEmpty())
            cbMethod.getSelectionModel().selectFirst();

    }

}
