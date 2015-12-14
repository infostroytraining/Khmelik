package db;

import dao.exceptions.DaoException;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ValidationException;

public interface Transaction<T> {
    
    public T execute() throws DaoException, ValidationException, DuplicateInsertException;

}
