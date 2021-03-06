package service.exceptions;

import java.util.List;

public class ValidationException extends Exception {

    private List<FieldError> fieldExceptions;

    public ValidationException(List<FieldError> fieldExceptions) {
        this.fieldExceptions = fieldExceptions;
    }

    public List<FieldError> getFieldExceptions() {
        return fieldExceptions;
    }
}
