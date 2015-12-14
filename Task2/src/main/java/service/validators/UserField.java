package service.validators;

public enum UserField implements Field {

    EMAIL("[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}","Please enter existing email."),
    NAME("\\p{L}{2,}","Name should contain 2 or more letters."),
    SURNAME("\\p{L}{2,}","Name should contain 2 or more letters."),
    PASSWORD("[a-zA-Z0-9]{4,20}","Password should contain latin letters and numbers only.\n" +
            "Password and confirmed password should be equal.\n" +
            "Password length should be between 4 and 20 symbols."),
    IMAGE(".*\\.(jpg|png|gif|jpeg)","Image should be gif, png or jpg/jpeg. Image max size = 5Mb.");

    private String pattern;
    private String userMessage;

    private UserField(String pattern, String userMessage) {
        this.pattern = pattern;
        this.userMessage = userMessage;
    }

    public String getPattern() {
        return pattern;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
