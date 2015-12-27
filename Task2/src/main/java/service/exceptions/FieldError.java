package service.exceptions;

import service.validators.Field;

public class FieldError extends Exception {

    protected String message;
    protected Field field;

    public FieldError(Field field) {
        this.message = "Error in input field '" + field.name() + "'. " + field.getUserMessage();
        this.field = field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}