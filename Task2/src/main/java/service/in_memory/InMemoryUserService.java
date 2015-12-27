package service.in_memory;

import dao.UserDao;
import dao.exceptions.DaoException;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import service.exceptions.FieldError;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import service.exceptions.DuplicateInsertException;
import service.UserService;
import service.validators.UserField;
import service.validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserService implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDao userDao;
    private Validator<User> userValidator;

    public InMemoryUserService(UserDao userDao, Validator<User> userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        logger.info("MockUserService initialized.");
    }

    @Override
    public User register(User user) throws ValidationException, ServiceException {
        logger.entry(user);
        userValidator.validate(user);
        String imageFileName = user.getName() + user.getSurname() + ".jpg";
        user.setImage(Strings.isEmpty(user.getImage()) ? null : imageFileName);
        logger.debug("User image set to '{}'.", user.getImage());
        try {
            if (!userDao.isAlreadyCreated(user)) {
                User result =  userDao.create(user);
                logger.exit(result);
                return result;
            } else {
                logger.warn("User duplicated insert.");
                List<FieldError> errors = new ArrayList<>();
                errors.add(new DuplicateInsertException(UserField.EMAIL));
                throw new ValidationException(errors);
            }
        } catch (DaoException e) {
            logger.error("DaoException caused in mock user service. Check service initialization.", e);
            throw new ServiceException();
        }
    }

    //TODO getUserByUsername impl
    @Override
    public User loadUserByUsername(String username) {
        return null;
    }
}
