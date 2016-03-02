package email.com.gmail.ttsai0509.http.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestConfig {

    private StringProperty method;
    private StringProperty url;
    private StringProperty media;
    private StringProperty body;
    private StringProperty script;
    private MapProperty<String, String> headers;

    public RequestConfig() {
        method = new SimpleStringProperty();
        url = new SimpleStringProperty();
        media = new SimpleStringProperty();
        body = new SimpleStringProperty();
        script = new SimpleStringProperty();
        headers = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }

    public String getMethod() {
        return method.get();
    }

    public StringProperty methodProperty() {
        return method;
    }

    public void setMethod(String method) {
        this.method.set(method);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getMedia() {
        return media.get();
    }

    public StringProperty mediaProperty() {
        return media;
    }

    public void setMedia(String media) {
        this.media.set(media);
    }

    public String getBody() {
        return body.get();
    }

    public StringProperty bodyProperty() {
        return body;
    }

    public void setBody(String body) {
        this.body.set(body);
    }

    public String getScript() {
        return script.get();
    }

    public StringProperty scriptProperty() {
        return script;
    }

    public void setScript(String script) {
        this.script.set(script);
    }

    public ObservableMap<String, String> getHeaders() {
        return headers.get();
    }

    public MapProperty<String, String> headersProperty() {
        return headers;
    }

    public void setHeaders(ObservableMap<String, String> headers) {
        this.headers.set(headers);
    }

    /******************************************************************
     *                                                                *
     * Header ListChangeListener
     *                                                                *
     ******************************************************************/

    public final ListChangeListener<Pair<String, String>> headerListener = changeItr -> {
        while (changeItr.next()) {
            if (changeItr.wasAdded())
                for (Pair<String, String> added : changeItr.getAddedSubList())
                    getHeaders().put(added.getKey(), added.getValue());
            if (changeItr.wasRemoved())
                for (Pair<String, String> removed : changeItr.getRemoved())
                    getHeaders().remove(removed.getKey());
        }
    };

    /******************************************************************
     *                                                                *
     * Helper Methods
     *                                                                *
     ******************************************************************/

    public Request build() {

        Request.Builder builder = new Request.Builder();

        builder.url(getUrl());

        Set<Map.Entry<String, String>> items = getHeaders().entrySet();
        builder.headers(Headers.of(items.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));

        RequestBody body = null;
        String mediaStr = getMedia();
        String bodyStr = getBody();
        if (mediaStr != null && !mediaStr.isEmpty() && bodyStr != null && !bodyStr.isEmpty()) {
            body = RequestBody.create(MediaType.parse(mediaStr), bodyStr);
        }
        builder.method(getMethod(), body);

        return builder.build();
    }

    public RequestConfig copy() {
        RequestConfig config = new RequestConfig();
        config.method.set(getMethod());
        config.url.set(getUrl());
        config.media.set(getMedia());
        config.body.set(getBody());
        config.headers.putAll(getHeaders());
        config.script.set(getScript());
        return config;
    }

}
