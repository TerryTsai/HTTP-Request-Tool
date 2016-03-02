package email.com.gmail.ttsai0509.http.controller.cell;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class HeaderCell extends ListCell<Pair<String, String>> {

    @FXML public GridPane root;
    @FXML public Label content;
    @FXML public Button remove;

    @FXML
    public void initialize() {
        remove.setOnAction(event -> {
            if (getItem() != null) {
                Platform.runLater(() -> getListView().getItems().remove(getItem()));
            }
        });
    }

    @Override
    protected void updateItem(Pair<String, String> item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            content.setText(item.getKey() + " - " + item.getValue());
            setGraphic(root);
        }
    }
}
