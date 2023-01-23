package com.spring.service.interfaces;

import java.io.Serializable;
import java.util.List;


public interface GenericService<T, PK extends Serializable> {
  
    T get(PK id);
    List<T> getAll();

    void createOrUpdate(T persistentObject) throws Exception;

    void delete(T persistentObject);

    
}
