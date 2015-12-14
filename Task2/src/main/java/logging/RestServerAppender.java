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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Plugin(name = "RestServerAppender", category = "Core", elementType = "appender", printObject = true)
public final class RestServerAppender extends AbstractAppender {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    private String loggerServerURL;

    protected RestServerAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, String serverURL) {
        super(name, filter, layout, ignoreExceptions);
        loggerServerURL = serverURL;
    }

    @Override
    public void append(LogEvent event) {
        readLock.lock();
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(loggerServerURL);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("name", "logEvent"));
        urlParameters.add(new BasicNameValuePair("message", event.getMessage().getFormattedMessage()));
        urlParameters.add(new BasicNameValuePair("date", String.valueOf(event.getTimeMillis())));
        urlParameters.add(new BasicNameValuePair("type", event.getLevel().name()));
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            client.execute(post);
        } catch (IOException e) {
            LOGGER.error("Server appender is not working.", e);
        } finally {
            readLock.unlock();
        }
    }

    @PluginFactory
    public static RestServerAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("server") String serverURL,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
        if (name == null) {
            LOGGER.error("No name provided for RestServerAppender.");
            return null;
        }
        if (serverURL == null) {
            LOGGER.error("No server url provided for RestServerAppender.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new RestServerAppender(name, filter, layout, true, serverURL);
    }
}
