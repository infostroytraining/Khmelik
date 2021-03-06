package dao;

import entity.Entity;
import dao.exceptions.DaoException;

import java.util.List;

public interface DAO<T extends Entity> {

    T create(T entity) throws DaoException;

    T get(int idEntity) throws DaoException;

    List<T> getAll() throws DaoException;

    T update(T entity) throws DaoException;

    boolean delete(int idEntity) throws DaoException;

}
