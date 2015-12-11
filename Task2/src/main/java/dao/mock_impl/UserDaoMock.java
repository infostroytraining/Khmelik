package dao.mock_impl;

import dao.UserDao;
import entity.User;

import java.util.*;

public class UserDaoMock implements UserDao {

    public static List<User> users = new ArrayList<User>();

    @Override
    public User create(User entity) {
        entity.setId((users.size() == 0) ? 1 : (users.get(users.size() - 1).getId() + 1));  //autoincrement
        users.add(entity);
        return entity;
    }

    @Override
    public User get(int idEntity) throws NoSuchElementException {
        Optional<User> result = users.stream().filter(user -> idEntity == user.getId()).findFirst();
        if (!result.isPresent()) {
            throw new NoSuchElementException();
        }
        return result.get();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User update(User entity) {
        User user = get(entity.getId());
        users.set(users.indexOf(user), entity);
        return entity;
    }

    @Override
    public boolean delete(int idEntity) {
        User user = get(idEntity);
        users.remove(users.indexOf(user));
        return true;
    }

    @Override
    public boolean isAlreadyCreated(User entity) {
        return users.stream().anyMatch(user ->
                user.getEmail().equals(entity.getEmail()) ||
                        (user.getName().equals(entity.getName()) && user.getSurname().equals(entity.getSurname())));
    }
}
