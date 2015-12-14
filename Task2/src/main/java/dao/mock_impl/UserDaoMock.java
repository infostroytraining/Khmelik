package dao.mock_impl;

import dao.UserDao;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class UserDaoMock implements UserDao {

    private static final Logger logger = LogManager.getLogger();

    private static final List<User> users = new ArrayList<>();

    @Override
    public User create(User entity) {
        logger.entry(entity);
        entity.setId((users.size() == 0) ? 1 : (users.get(users.size() - 1).getId() + 1));  //autoincrement
        users.add(entity);
        logger.exit(entity);
        return entity;
    }

    @Override
    public User get(int idEntity) {
        logger.entry(idEntity);
        Optional<User> result = users.stream().filter(user -> idEntity == user.getId()).findFirst();
        logger.exit(result.get());
        return result.get();
    }

    @Override
    public List<User> getAll() {
        logger.entry();
        logger.exit(users);
        return users;
    }

    @Override
    public User update(User entity) {
        logger.entry(entity);
        User user = get(entity.getId());
        users.set(users.indexOf(user), entity);
        logger.exit(entity);
        return entity;
    }

    @Override
    public boolean delete(int idEntity) {
        logger.entry(idEntity);
        User user = get(idEntity);
        user = users.remove(users.indexOf(user));
        logger.exit(user != null);
        return user != null;
    }

    @Override
    public boolean isAlreadyCreated(User entity) {
        logger.entry(entity);
        return users.stream().anyMatch(user ->
                user.getEmail().equals(entity.getEmail()) ||
                        (user.getName().equals(entity.getName()) && user.getSurname().equals(entity.getSurname())));
    }

    @Override
    public User getUserByUsername(String username) {
        logger.entry(username);
        Optional<User> result = users.stream().filter(user ->
                username.equals(user.getEmail())).findFirst();
        logger.exit(result.get());
        return result.get();
    }
}
