package service.mock_impl;

import dao.UserDao;
import dao.exceptions.DaoException;
import entity.User;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import service.exceptions.DuplicateInsertException;
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
    public User register(User user) throws ValidationException, DuplicateInsertException, ServiceException {
        userValidator.validate(user);
        if (user.getImage() != null) {
            user.setImage("/images/" + user.getName() + user.getSurname() + ".jpg");
        }
        try {
            if (!userDao.isAlreadyCreated(user)) {
                return userDao.create(user);
            } else {
                throw new DuplicateInsertException();
            }
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    //TODO getUserByUsername impl
    @Override
    public User loadUserByUsername(String username) {
        return null;
    }
}
