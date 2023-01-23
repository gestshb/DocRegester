/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.ActionDao;
import com.spring.entity.Action;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * @author pc
 */
@Repository
public class ActionDaoImpl extends GenericDaoImpl<Action, String> implements ActionDao {

    @Autowired
    public ActionDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Action.class);
    }

    @Override
    public List<Action> findByName(String action) throws DataAccessException {
        String query = "select  from Action u  where  u.action like :action";
        Query hql = getCurrentSession().createQuery(query);
        hql.setString("action", "%" + action + "%");
        List<Action> list = hql.list();
        return list;
    }

    


}
