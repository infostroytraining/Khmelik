package service;

import db.exceptions.TransactionException;
import entity.User;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import service.exceptions.DuplicateInsertException;

public interface UserService {

    User register(User user) throws ValidationException, ServiceException, TransactionException;

    User loadUserByUsername(String username) throws TransactionException, ValidationException;

}
