package web;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public final class HttpSender {

    private static final Logger logger = LogManager.getLogger("httpSender");
    private static HttpClient syncClient = HttpClientBuilder.create().build();

    public static void sendAsyncPostRequest(String url, List<NameValuePair> parameters) throws IOException {
            URL serverUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(parametersToRequestBodyString(parameters));
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            logger.debug("Response code: " + responseCode);
    }

    public static HttpResponse sendSyncPostRequest(String url, List<NameValuePair> parameters) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters));
        return syncClient.execute(post);
    }

    private static String parametersToRequestBodyString(List<NameValuePair> parameters) {
        if (parameters.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        parameters.stream().forEach(parameter ->
                builder.append(parameter.getName()).append('=').append(parameter.getValue()).append('&'));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

}