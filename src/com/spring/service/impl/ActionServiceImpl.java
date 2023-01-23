/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.ActionDao;
import com.spring.entity.Action;
import com.spring.service.interfaces.ActionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pc
 */
@Service
@Transactional
public class ActionServiceImpl  implements ActionService {
    @Autowired
    ActionDao d;

    @Override
    public List<Action> findByName(String action) {
       return d.findByName(action);
    }

   

    @Override
    public List<Action> getAll() {
      return d.getAll();
    }

   
    @Override
    public void createOrUpdate(Action persistentObject) throws Exception {
       d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(Action persistentObject) {
       d.delete(persistentObject);
    }

    @Override
    public Action get(String id) {
       return d.get(null);
    }

   
    


}
