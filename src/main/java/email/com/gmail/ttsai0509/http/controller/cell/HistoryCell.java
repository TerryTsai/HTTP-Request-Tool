package email.com.gmail.ttsai0509.http.controller.cell;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class HistoryCell extends ListCell<RequestConfig> {

    @FXML public GridPane root;
    @FXML public Label content;
    @FXML public Button use;
    @FXML public Button remove;

    @FXML
    public void initialize() {
        use.setOnAction(event -> {
            if (getItem() != null) {
                Platform.runLater(() -> {
                    HttpRequestTool.requestController.bindRequest(getItem().copy());
                });
            }
        });

        remove.setOnAction(event -> {
            if (getItem() != null) {
                Platform.runLater(() -> getListView().getItems().remove(getItem()));
            }
        });
    }

    @Override
    protected void updateItem(RequestConfig item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            content.setText(item.getMethod() + " " + item.getUrl());
            setGraphic(root);
        }
    }

}