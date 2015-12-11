package service.mock_impl;

import dao.UserDao;
import entity.User;
import exceptions.ValidationException;
import exceptions.DuplicateInsertException;
import service.UserService;
import service.validators.Validator;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private Validator<User> userValidator;

    public UserServiceImpl(UserDao userDao, Validator<User> userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    @Override
    public User register(User user) throws ValidationException, DuplicateInsertException {
        userValidator.validate(user);
        if(!userDao.isAlreadyCreated(user)) {
            return userDao.create(user);
        } else {
            throw new DuplicateInsertException();
        }
    }

    //TODO getUserByUsername impl
    @Override
    public User loadUserByUsername(String username) {
        return null;
    }
}
