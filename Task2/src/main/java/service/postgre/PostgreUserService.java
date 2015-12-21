package service.postgre;

import dao.UserDao;
import db.TransactionManager;
import db.exceptions.TransactionException;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import service.UserService;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ValidationException;
import service.validators.UserValidator;
import service.validators.Validator;

import java.sql.Connection;

public class PostgreUserService implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDao userDao;
    private TransactionManager transactionManager;
    private Validator<User> userValidator;

    public PostgreUserService(UserDao userDao, UserValidator userValidator,
                              TransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
        this.userValidator = userValidator;
        logger.info("PostgreUserService initialized.");
    }

    @Override
    public User register(User user) throws ValidationException, DuplicateInsertException, TransactionException {
        return transactionManager.doTask(() -> {
            logger.entry(user);
            userValidator.validate(user);
            String imageFileName = user.getName() + user.getSurname() + ".jpg";
            user.setImage(Strings.isEmpty(user.getImage()) ? null : imageFileName);
            logger.debug("User image set to '{}'.", user.getImage());
            if (!userDao.isAlreadyCreated(user)) {
                User result =  userDao.create(user);
                logger.exit(result);
                return result;
            } else {
                logger.warn("User duplicated insert.");
                throw new DuplicateInsertException();
            }
        }, Connection.TRANSACTION_READ_COMMITTED);
    }

    @Override
    public User loadUserByUsername(String username) throws TransactionException,
            DuplicateInsertException, ValidationException {
        logger.entry(username);
        return transactionManager.doTask(() ->
                userDao.getUserByUsername(username), Connection.TRANSACTION_READ_COMMITTED);
    }
}