package service;

import entity.User;
import exceptions.ValidationException;
import exceptions.DuplicateInsertException;

public interface UserService {

    User register(User user) throws ValidationException, DuplicateInsertException;

    User loadUserByUsername(String username);

}
