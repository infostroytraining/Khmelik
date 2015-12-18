package web.captcha;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import web.HttpSender;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GoogleReCaptchaValidationUtils {

    private static final String SECRET_KEY = "6Len4RITAAAAADJd7twnHBFx8SbAp0tFBlBS9BDT";
    private static final String GOOGLE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validate(String gRecaptchaResponse) throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("secret", SECRET_KEY));
        urlParameters.add(new BasicNameValuePair("response", gRecaptchaResponse));

        HttpResponse response = HttpSender.sendSyncPostRequest(GOOGLE_VERIFY_URL, urlParameters);
        String responseContent = IOUtils.toString(response.getEntity().getContent());

        JsonReader jsonReader = Json.createReader(new StringReader(responseContent));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return jsonObject.getBoolean("success");
    }
}