package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class FinderController implements AppCtrl<HttpRequestTool> {

    @FXML public ListView<RequestConfig> lvHistory;
    @FXML public ListView lvCollections;
    @FXML public Accordion root;

    @Override
    public void postLoad(HttpRequestTool app) {

        lvHistory.setPlaceholder(new Label("No requests yet."));
        lvHistory.setCellFactory(param -> AppCtrl.loadGetCtrl(getClass().getResource("/fxml/cell/history-cell.fxml"), app));
        lvHistory.setItems(app.getHistory());

        lvCollections.setPlaceholder(new Label("Coming soon."));

    }

}
