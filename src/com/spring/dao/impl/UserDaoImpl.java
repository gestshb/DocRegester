/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.UserDao;
import com.spring.entity.Action;
import com.spring.entity.User;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * @author pc
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    public List<User> getByLikeName(String name) {
        String query = "from User u  where  u.username like :name  ";
        Query hql = getCurrentSession().createQuery(query);
        hql.setString("name", "%" + name + "%");
        List<User> list = hql.list();
        return list;
    }

    @Override
    public Set<Action> getActions(Long iduser) {
        User user = get(iduser);
        return user.getActions();
    }

    @Override
    public void addActiontoUser(Set<Action> roles, User user) {
        user.setActions(roles);
        try {
            createOrUpdate(user);
        } catch (Exception ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public User getByName(String name) {
        String query = "from User u  where  u.username = :name";
        Query hql = getCurrentSession().createQuery(query);
        hql.setString("name", name);
        return (User) hql.uniqueResult();

    }

   


}
