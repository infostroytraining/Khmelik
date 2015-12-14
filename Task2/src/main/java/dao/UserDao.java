package dao;

import dao.exceptions.DaoException;
import entity.User;

public interface UserDao extends DAO<User> {

    /**
     * You need this method, because there is no DuplicatedInsertException in JDBC.
     * If user already exists, system will return SQLException.
     */
    boolean isAlreadyCreated(User user) throws DaoException;

    User getUserByUsername(String username) throws DaoException;

}
