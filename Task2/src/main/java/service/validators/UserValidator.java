package service.validators;

import entity.User;
import exceptions.FieldError;
import exceptions.ValidationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserValidator implements Validator<User> {

    private static final Logger LOGGER = Logger.getLogger(UserValidator.class);

    @Override
    public void validate(User user) throws ValidationException {
        List<FieldError> errors = new ArrayList<>();
        if (user == null) {
            LOGGER.error("Registration/inserting user is null.");
            throw new IllegalArgumentException("Registration/inserting user is null.");
        }
        if (user.getEmail() == null
               || !user.getEmail().matches(UserField.EMAIL.getPattern())) {
            errors.add(new FieldError(UserField.EMAIL));
        }
        if (user.getName() == null||
                !user.getName().matches(UserField.NAME.getPattern())) {
            errors.add(new FieldError(UserField.NAME));
        }
        if (user.getSurname() == null
               || !user.getSurname().matches(UserField.SURNAME.getPattern())) {
            errors.add(new FieldError(UserField.SURNAME));
        }
        if (user.getPassword() == null
               || !user.getPassword().matches(UserField.PASSWORD.getPattern())) {
            errors.add(new FieldError(UserField.PASSWORD));
        }

        if (errors.size() != 0) {
            throw new ValidationException(errors);
        }
    }
}
