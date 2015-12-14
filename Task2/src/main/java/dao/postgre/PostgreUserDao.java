package dao.postgre;

import dao.UserDao;
import db.ConnectionHolder;
import entity.User;
import dao.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PostgreUserDao implements UserDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String INSERT_USER_QUERY =
            "INSERT INTO users(email, name, surname, password, image) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE idUser=?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET(email=?, name=?, surname=?, password=?, image=?) WHERE idUser=?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE idUser=?";
    private static final String GET_USER_BY_UNIQUE_FIELDS =
            "SELECT * FROM users WHERE email=? OR (name=? AND surname=?)";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE email=?";


    @Override
    public boolean isAlreadyCreated(User user) throws DaoException {
        logger.entry(user);
        boolean result;
        Connection connection = ConnectionHolder.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_UNIQUE_FIELDS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            ResultSet resultSet = preparedStatement.executeQuery();
            result = resultSet.next();
        } catch (SQLException e) {
            logger.error("Cannot find user in database.", e);
            throw new DaoException(e);
        }
        logger.exit(result);
        return result;
    }

    @Override
    public User getUserByUsername(String username) throws DaoException {
        logger.entry(username);
        Connection connection = ConnectionHolder.getConnection();
        User resultUser = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultUser = new User();
                resultUser.setId(resultSet.getInt(1));
                resultUser.setEmail(resultSet.getString(2));
                resultUser.setName(resultSet.getString(3));
                resultUser.setSurname(resultSet.getString(4));
                resultUser.setPassword(resultSet.getString(5));
                resultUser.setImage(resultSet.getString(6));
            }
        } catch (SQLException e) {
            logger.error("Cannot find user in database.", e);
            throw new DaoException(e);
        }
        logger.exit(resultUser);
        return resultUser;
    }

    @Override
    public User create(User entity) throws DaoException {
        logger.entry(entity);
        Connection connection = ConnectionHolder.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSurname());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getImage());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            logger.error("Cannot insert user into database.", e);
            throw new DaoException(e);
        }
        logger.exit();
        return entity;
    }

    @Override
    public User get(int idEntity) throws DaoException {
        logger.entry(idEntity);
        Connection connection = ConnectionHolder.getConnection();
        User resultUser = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_QUERY);
            preparedStatement.setInt(1, idEntity);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultUser = new User();
                resultUser.setId(resultSet.getInt(1));
                resultUser.setEmail(resultSet.getString(2));
                resultUser.setName(resultSet.getString(3));
                resultUser.setSurname(resultSet.getString(4));
                resultUser.setPassword(resultSet.getString(5));
                resultUser.setImage(resultSet.getString(6));
            }
        } catch (SQLException e) {
            logger.error("Cannot get user from database.", e);
            throw new DaoException(e);
        }
        logger.exit(resultUser);
        return resultUser;
    }

    @Override
    public List<User> getAll() throws DaoException {
        List<User> resultUsers = new ArrayList<>();
        Connection connection = ConnectionHolder.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);
            while (resultSet.next()) {
                User tempUser = new User();
                tempUser.setId(resultSet.getInt(1));
                tempUser.setEmail(resultSet.getString(2));
                tempUser.setName(resultSet.getString(3));
                tempUser.setSurname(resultSet.getString(4));
                tempUser.setPassword(resultSet.getString(5));
                tempUser.setImage(resultSet.getString(6));
                resultUsers.add(tempUser);
            }
        } catch (SQLException e) {
            logger.error("Cannot get all users from database.", e);
            throw new DaoException(e);
        }
        logger.exit(resultUsers);
        return resultUsers;
    }

    @Override
    public User update(User entity) throws DaoException {
        logger.entry(entity);
        Connection connection = ConnectionHolder.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSurname());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getImage());
            preparedStatement.setInt(6, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Cannot update user in database.", e);
            throw new DaoException(e);
        }
        logger.exit(entity);
        return entity;
    }

    @Override
    public boolean delete(int idEntity) throws DaoException {
        logger.entry(idEntity);
        boolean result;
        Connection connection = ConnectionHolder.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, idEntity);
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            logger.error("Cannot delete user from database.", e);
            throw new DaoException(e);
        }
        logger.exit(result);
        return result;
    }
}