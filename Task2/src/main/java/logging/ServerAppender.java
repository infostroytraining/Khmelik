package logging;

import org.apache.http.NameValuePair;
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
import utils.HttpSender;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Plugin(name = "ServerAppender", category = "Core", elementType = "appender", printObject = true)
public final class ServerAppender extends AbstractAppender {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    private String loggerServerURL;

    protected ServerAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                             boolean ignoreExceptions, String serverURL) {
        super(name, filter, layout, ignoreExceptions);
        loggerServerURL = serverURL;
        LOGGER.debug("Server appender created.");
    }

    @Override
    public void append(LogEvent event) {
        readLock.lock();
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("name", "logEvent"));
        parameters.add(new BasicNameValuePair("message", event.getMessage().getFormattedMessage()));
        parameters.add(new BasicNameValuePair("date", String.valueOf(event.getTimeMillis())));
        parameters.add(new BasicNameValuePair("type", event.getLevel().name()));
        try {
            HttpSender.sendAsyncPostRequest(loggerServerURL, parameters);
        } catch (IOException e) {
            LOGGER.debug("IOException in HttpSender.", e);
        } finally {
            readLock.unlock();
        }
    }

    @PluginFactory
    public static ServerAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("server") String serverURL,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
        if (name == null) {
            LOGGER.error("No name provided for ServerAppender.");
            return null;
        }
        if (serverURL == null) {
            LOGGER.error("No server url provided for ServerAppender.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new ServerAppender(name, filter, layout, true, serverURL);
    }
}