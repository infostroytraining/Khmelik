package service.validators;

import service.exceptions.ValidationException;

public interface Validator<T> {

    void validate(T object) throws ValidationException;

}
