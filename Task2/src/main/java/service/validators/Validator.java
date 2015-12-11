package service.validators;

import exceptions.ValidationException;

public interface Validator<T> {

    void validate(T object) throws ValidationException;

}
