package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.utils.AppController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import okhttp3.Headers;
import okhttp3.Response;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class ResponseController implements AppController<HttpRequestTool> {

    @FXML public StackPane root;
    @FXML public ListView<Pair<String, String>> lvResponseNetwork;
    @FXML public ListView<Pair<String, String>> lvResponseHeaders;
    @FXML public TextFlow tfResponseBody;
    @FXML public WebView wvResponseBody;

    private Tidy tidy;

    @Override
    public void initialize(HttpRequestTool app) {
        this.tidy = app.getTidy();
        lvResponseNetwork.setItems(FXCollections.observableArrayList());
        lvResponseHeaders.setItems(FXCollections.observableArrayList());
    }

    /* Response */

    private Response response;

    public void setResponse(Response response) {

        if (this.response != null) {
            // Clear control response bindings and residual data
            lvResponseNetwork.getItems().clear();
            lvResponseHeaders.getItems().clear();
            tfResponseBody.getChildren().clear();
            wvResponseBody.getEngine().loadContent("");
        }

        this.response = response;

        if (this.response != null) {
            // Setup controls with new response data
            Response network = this.response.networkResponse();
            lvResponseNetwork.getItems().add(new Pair<>("Code", "" + network.code()));
            lvResponseNetwork.getItems().add(new Pair<>("Message", network.message()));
            if (network.protocol() != null)
                lvResponseNetwork.getItems().add(new Pair<>("Protocol", network.protocol().name()));
            if (network.handshake() != null)
                lvResponseNetwork.getItems().add(new Pair<>("Handshake", network.handshake().toString()));

            Headers headers = this.response.headers();
            for (String header : headers.names())
                lvResponseHeaders.getItems().add(new Pair<>(header, headers.get(header)));

            OutputStream out = new ByteArrayOutputStream();
            Document htmlDOM = tidy.parseDOM(this.response.body().byteStream(), null);
            tidy.pprint(htmlDOM, out);
            String result = out.toString();

            Text text = new Text(result);
            text.setFont(Font.font("Lucida Console"));
            tfResponseBody.getChildren().setAll(text);
            wvResponseBody.getEngine().loadContent(result);

        }

    }

}
