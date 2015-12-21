package service.validators;

import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import service.exceptions.FieldError;
import service.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UserValidator implements Validator<User> {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void validate(User user) throws ValidationException {
        logger.entry(user);
        List<FieldError> errors = new ArrayList<>();
        if (user == null) {
            logger.error("Registration/inserting user is null.");
            throw new IllegalArgumentException("Registration/inserting user is null.");
        }
        if (Strings.isEmpty(user.getEmail())
                || !user.getEmail().matches(UserField.EMAIL.getPattern())) {
            errors.add(new FieldError(UserField.EMAIL));
        }
        if (Strings.isEmpty(user.getName()) ||
                !user.getName().matches(UserField.NAME.getPattern())) {
            errors.add(new FieldError(UserField.NAME));
        }
        if (Strings.isEmpty(user.getSurname())
                || !user.getSurname().matches(UserField.SURNAME.getPattern())) {
            errors.add(new FieldError(UserField.SURNAME));
        }
        if (Strings.isEmpty(user.getPassword())
                || !user.getPassword().matches(UserField.PASSWORD.getPattern())) {
            errors.add(new FieldError(UserField.PASSWORD));
        }
        if (!Strings.isEmpty(user.getImage())
                && !user.getImage().matches(UserField.IMAGE.getPattern())) {
            errors.add(new FieldError(UserField.IMAGE));
        }
        if (errors.size() != 0) {
            throw new ValidationException(errors);
        }
        logger.exit("User successfully validated.");
    }
}