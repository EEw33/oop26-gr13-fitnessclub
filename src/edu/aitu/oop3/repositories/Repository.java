package edu.aitu.oop3.repositories;

import java.util.List;

public interface Repository<T, ID> {
    T create(T entity);
    T findById(ID id);
    List<T> findAll();
}
