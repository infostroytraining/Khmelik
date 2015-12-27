package service.exceptions;

import service.validators.Field;

public class DuplicateInsertException extends FieldError {

    public DuplicateInsertException(Field field) {
        super(field);
        this.message = "User with such " + field.name().toLowerCase() + " already exists.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
