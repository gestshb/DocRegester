/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.TypeDao;
import com.spring.entity.Type;
import com.spring.service.interfaces.TypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pc
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {
    
    @Autowired
    TypeDao d;

    @Override
    public Type get(Long id) {
       return d.get(id);
    }

    @Override
    public List<Type> getAll() {
      return d.getAll();
    }

    @Override
    public void createOrUpdate(Type persistentObject) throws Exception {
        d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(Type persistentObject) {
       d.delete(persistentObject);
    }


}
