package dao;

import entity.Entity;

import java.util.List;

public interface DAO<T extends Entity> {

    T create(T entity);

    T get(int idEntity);

    List<T> getAll();

    T update(T entity);

    boolean delete(int idEntity);

}
