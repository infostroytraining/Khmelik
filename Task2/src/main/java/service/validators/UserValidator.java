package service.validators;

import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.FieldError;
import service.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UserValidator implements Validator<User> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void validate(User user) throws ValidationException {
        LOGGER.debug("User sent on validation: {}", user);
        List<FieldError> errors = new ArrayList<>();
        if (user == null) {
            LOGGER.error("Registration/inserting user is null.");
            throw new IllegalArgumentException("Registration/inserting user is null.");
        }
        if (user.getEmail() == null
                || !user.getEmail().matches(UserField.EMAIL.getPattern())) {
            errors.add(new FieldError(UserField.EMAIL));
        }
        if (user.getName() == null ||
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
        if (!(user.getImage() == null || user.getImage().isEmpty())
                && !user.getImage().matches(UserField.IMAGE.getPattern())) {
            errors.add(new FieldError(UserField.IMAGE));
        }
        if (errors.size() != 0) {
            throw new ValidationException(errors);
        }
    }
}
