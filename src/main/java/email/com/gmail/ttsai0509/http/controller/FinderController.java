package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class FinderController implements AppCtrl<HttpRequestTool> {

    @FXML public Accordion root;
    @FXML public ListView<RequestConfig> lvHistory;
    @FXML public ComboBox<StringProperty> cbCollections;
    @FXML public ListView<RequestConfig> lvCollection;
    @FXML public GridPane gpCollectionsContainer;

    @Override
    public void postLoad(HttpRequestTool app) {

        // HACK : ComboBox fit-to-width
        cbCollections.prefWidthProperty().bind(gpCollectionsContainer.widthProperty());
        cbCollections.setPlaceholder(new Label("No collections."));

        lvHistory.setPlaceholder(new Label("No requests yet."));
        lvHistory.setCellFactory(param -> app.quickCtrl("/fxml/cell/history-cell.fxml"));
        lvHistory.setItems(app.getHistory());

        lvCollection.setPlaceholder(new Label("Coming soon."));

    }

}
