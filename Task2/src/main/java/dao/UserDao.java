package dao;

import entity.User;

public interface UserDao extends DAO<User> {

    boolean isAlreadyCreated(User email);

}
