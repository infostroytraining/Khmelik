package service.mock_impl;

import dao.UserDao;
import dao.exceptions.DaoException;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import service.exceptions.DuplicateInsertException;
import service.UserService;
import service.validators.Validator;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDao userDao;
    private Validator<User> userValidator;

    public UserServiceImpl(UserDao userDao, Validator<User> userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        logger.info("MockUserService initialized.");
    }

    @Override
    public User register(User user) throws ValidationException, DuplicateInsertException, ServiceException {
        logger.entry(user);
        userValidator.validate(user);
        String imageFileName = user.getName() + user.getSurname() + ".jpg";
        user.setImage((user.getImage() == null || user.getImage().isEmpty()) ? null : imageFileName);
        logger.debug("User image set to '{}'.", user.getImage());
        try {
            if (!userDao.isAlreadyCreated(user)) {
                User result =  userDao.create(user);
                logger.exit(result);
                return result;
            } else {
                logger.warn("User duplicated insert.");
                throw new DuplicateInsertException();
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
