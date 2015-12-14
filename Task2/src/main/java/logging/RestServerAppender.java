package logging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Plugin(name = "RestServerAppender", category = "Core", elementType = "appender", printObject = true)
public final class RestServerAppender extends AbstractAppender {

    public static final String LOGGER_SERVER_URL = "http://localhost:8080/server-log-app/logs";

    protected RestServerAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(LOGGER_SERVER_URL);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("name", "logEvent"));
        urlParameters.add(new BasicNameValuePair("value", event.getMessage().getFormattedMessage()));
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() != 200){
                LOGGER.error("Server appender is not working. Answer status code is not equal 200.");
            }
        } catch (IOException e) {
            LOGGER.error("Server appender is not working.", e);
        }
    }

    @PluginFactory
    public static RestServerAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
            if (name == null) {
                LOGGER.error("No name provided for RestServerAppender.");
                return null;
            }
            if (layout == null) {
                layout = PatternLayout.createDefaultLayout();
            }
        return new RestServerAppender(name, filter, layout, true);
    }
}
