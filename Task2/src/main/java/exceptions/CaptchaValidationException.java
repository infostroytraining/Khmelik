package exceptions;

public class CaptchaValidationException extends Exception {

    private String captchaApiProvider;

    public CaptchaValidationException(String captchaApiProvider) {
        this.captchaApiProvider = captchaApiProvider;
    }

    @Override
    public String getMessage() {
        return "Captcha: " + captchaApiProvider
                + ". Exception: Captcha validation failed.";
    }
}
