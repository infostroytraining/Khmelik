package service.postgre;

import dao.UserDao;
import db.TransactionManager;
import db.exceptions.TransactionException;
import entity.User;
import service.UserService;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import service.validators.UserValidator;
import service.validators.Validator;

import java.sql.Connection;

public class PostgreUserService implements UserService {

    private UserDao userDao;
    private TransactionManager transactionManager;
    private Validator<User> userValidator;

    public PostgreUserService(UserDao userDao, UserValidator userValidator,
                              TransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
        this.userValidator = userValidator;
    }

    @Override
    public User register(User user) throws ValidationException, DuplicateInsertException, ServiceException, TransactionException {
        return transactionManager.doTask(() -> {
            userValidator.validate(user);
            if (user.getImage() != null)
                user.setImage(user.getName() + user.getSurname() + ".jpg");

            if (!userDao.isAlreadyCreated(user)) {
                return userDao.create(user);
            } else {
                throw new DuplicateInsertException();
            }
        }, Connection.TRANSACTION_READ_COMMITTED);
    }

    @Override
    public User loadUserByUsername(String username) throws TransactionException,
            DuplicateInsertException, ValidationException {
        return transactionManager.doTask(() ->
                userDao.getUserByUsername(username), Connection.TRANSACTION_READ_COMMITTED);
    }

}
