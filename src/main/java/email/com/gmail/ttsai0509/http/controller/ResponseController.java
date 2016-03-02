package email.com.gmail.ttsai0509.http.controller;

import email.com.gmail.ttsai0509.http.HttpRequestTool;
import email.com.gmail.ttsai0509.http.model.RequestConfig;
import email.com.gmail.ttsai0509.http.utils.AppCtrl;
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

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

public class ResponseController implements AppCtrl<HttpRequestTool> {

    @FXML public StackPane root;
    @FXML public ListView<Pair<String, String>> lvResponseNetwork;
    @FXML public ListView<Pair<String, String>> lvResponseHeaders;
    @FXML public TextFlow tfResponseBody;
    @FXML public WebView wvResponseBody;
    @FXML public TextFlow tfScriptConsole;

    private Tidy tidy;
    private ScriptEngine jsEngine;

    @Override
    public void postLoad(HttpRequestTool app) {
        this.tidy = app.getTidy();
        this.jsEngine = app.getJsEngine();

        jsEngine.getContext().setWriter(new StringWriter() {
            @Override
            public void flush() {
                tfScriptConsole.getChildren().add(new Text(getBuffer().toString()));
                getBuffer().setLength(0);
            }
        });

        lvResponseNetwork.setItems(FXCollections.observableArrayList());
        lvResponseHeaders.setItems(FXCollections.observableArrayList());
    }

    /* Response */

    private Response response;

    public void setResult(RequestConfig config, Response response) {

        if (this.response != null) {
            // Clear control response bindings and residual data
            lvResponseNetwork.getItems().clear();
            lvResponseHeaders.getItems().clear();
            tfResponseBody.getChildren().clear();
            tfScriptConsole.getChildren().clear();
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

            if (config.getScript() != null) {
                try {
                    jsEngine.put("document", result);
                    jsEngine.eval(config.getScript());
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
