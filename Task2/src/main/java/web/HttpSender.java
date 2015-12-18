package web;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class HttpSender {

    private static final Logger logger = LogManager.getLogger("httpSender");

    private static HttpClient syncClient = HttpClientBuilder.create().build();
    private static CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault();

    public static void sendAsyncPostRequest(String url, List<NameValuePair> parameters) throws ExecutionException,
            InterruptedException, UnsupportedEncodingException {
//        if (!asyncClient.isRunning()) {
//            asyncClient.start();
//        }
//        HttpPost post = new HttpPost(url);
//        post.setEntity(new UrlEncodedFormEntity(parameters));
///*        asyncClient.execute(post, new FutureCallback<HttpResponse>() {
//            @Override
//            public void completed(HttpResponse httpResponse) {
//                logger.debug("Logging server request completed.");
//                post.releaseConnection();;
//            }
//
//            @Override
//            public void failed(Exception e) {
//                logger.debug("Logging server request failed.");
//                post.abort()
//            }
//
//            @Override
//            public void cancelled() {
//                logger.debug("Logging server request canceled.");
//            }
//        });
//*/
//        asyncClient.execute(post, null);
//        try {
//            asyncClient.close();
//        } catch (IOException e) {
//            logger.debug("IOException while closing closeable client", e);
//        }

        try {
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
        } catch (MalformedURLException e) {
            logger.debug("Malformed URL in HttpSender.", e);
        } catch (IOException e) {
            logger.debug("IOException in HttpSender.", e);
        }
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