/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.UserDao;
import com.spring.entity.Action;
import com.spring.entity.User;
import com.spring.service.interfaces.UserService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pc
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao d;

    @Override
    public List<User> getByLikeName(String number) {
      return d.getByLikeName(number);
    }

    @Override
    public User getByName(String name) {
       return d.getByName(name);
    }

    @Override
    public Set<Action> getActions(Long iduser) {
       return d.getActions(iduser);
    }

    @Override
    public void addActiontoUser(Set<Action> action, User user) {
      d.addActiontoUser(action, user);
    }

    @Override
    public User get(Long id) {
       return d.get(id);
    }

    @Override
    public List<User> getAll() {
       return d.getAll();
    }

    @Override
    public void createOrUpdate(User persistentObject) throws Exception {
        d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(User persistentObject) {
      d.delete(persistentObject);
    }
    
   


}
