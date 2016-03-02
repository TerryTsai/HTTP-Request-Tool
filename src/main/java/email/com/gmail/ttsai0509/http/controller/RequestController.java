package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.FXMLUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import okhttp3.Request;

import java.util.stream.Collectors;

public class RequestController {

    @FXML public Accordion root;
    @FXML public TextField tfUrl;
    @FXML public ComboBox<String> cbMethod;
    @FXML public TextField tfKey;
    @FXML public TextField tfValue;
    @FXML public ListView<Pair<String, String>> lvHeaders;
    @FXML public Button btnHeaderAdd;
    @FXML public TextArea taBody;
    @FXML public TextField tfType;

    @FXML
    public void initialize() {

        cbMethod.setItems(FXCollections.observableArrayList("GET", "POST", "PUT", "DELETE"));

        lvHeaders.setPlaceholder(new Label("Nothing to see here."));
        lvHeaders.setCellFactory(param -> FXMLUtils.loadAndGetCtrl(getClass().getResource("/header-cell.fxml")));
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

    /* RequestConfig Binding */

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
            lvHeaders.getItems().removeListener(this.request.headerListener);

            // Clear residual RequestConfig data
            tfUrl.setText("");
            taBody.setText("");
            tfType.setText("");
            cbMethod.getSelectionModel().clearSelection();
            lvHeaders.getItems().clear();
        }

        this.request = request;

        if (this.request != null) {
            // Update controls with new RequestConfig data
            tfUrl.setText(this.request.getUrl());
            taBody.setText(this.request.getBody());
            tfType.setText(this.request.getMedia());
            cbMethod.getSelectionModel().select(this.request.getMethod());
            lvHeaders.getItems().setAll(this.request.getHeaders().entrySet()
                            .stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toList())
            );

            // Bind RequestConfig to controls for changes
            this.request.urlProperty().bind(tfUrl.textProperty());
            this.request.bodyProperty().bind(taBody.textProperty());
            this.request.mediaProperty().bind(tfType.textProperty());
            this.request.methodProperty().bind(cbMethod.getSelectionModel().selectedItemProperty());
            lvHeaders.getItems().addListener(this.request.headerListener);
        }
    }
}
