package web.captcha;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GoogleReCaptchaValidationUtils {

    private static final String secretKey = "6Len4RITAAAAADJd7twnHBFx8SbAp0tFBlBS9BDT";
    private static final String url = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validate(String gRecaptchaResponse) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("secret", secretKey));
        urlParameters.add(new BasicNameValuePair("response", gRecaptchaResponse));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        String responseContent = IOUtils.toString(response.getEntity().getContent());

        JsonReader jsonReader = Json.createReader(new StringReader(responseContent));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return jsonObject.getBoolean("success");
    }
}
