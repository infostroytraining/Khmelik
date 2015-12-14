package service.exceptions;

import service.validators.Field;

public class FieldError extends Exception {

    private String message;

    public FieldError(Field field) {
        this.message = "Error in input field '" + field.name() + "'. " + field.getUserMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}