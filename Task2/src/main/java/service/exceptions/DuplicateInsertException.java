package service.exceptions;

import service.validators.Field;

public class DuplicateInsertException extends FieldError {

    private static final String MESSAGE = "User with such email or name+surname combination already exists.";

    public DuplicateInsertException() {}

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
