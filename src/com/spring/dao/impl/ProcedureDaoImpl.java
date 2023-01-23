/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.ProcedureDao;
import com.spring.entity.Procedure;
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
public class ProcedureDaoImpl extends GenericDaoImpl<Procedure, Long> implements ProcedureDao {

    @Autowired
    public ProcedureDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Procedure.class);
    }

    @Override
    public List<Procedure> findByName(String discription) throws DataAccessException {
        String query = "select  from Procedure u  where  u.discription like :discription";
        Query hql = getCurrentSession().createQuery(query);
        hql.setString("discription", "%" + discription + "%");
        List<Procedure> list = hql.list();
        return list;
       
    }


}
