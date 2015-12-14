package db;

import dao.exceptions.DaoException;

public interface Transaction<T> {
    
    public T execute() throws DaoException;

}
