package com.spring.dao.interfaces;

import java.io.Serializable;
import java.util.List;


public interface GenericDao<T, PK extends Serializable> {
    PK create(T persistentObject);

    T get(PK id);

    List<T> getAll();

    void update(T persistentObject);

    void createOrUpdate(T persistentObject) throws Exception;

    void delete(T persistentObject);

    void deleteById(PK id);
}
