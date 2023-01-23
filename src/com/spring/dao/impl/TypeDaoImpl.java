/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.TypeDao;
import com.spring.entity.Type;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author pc
 */

@Repository
public class TypeDaoImpl extends GenericDaoImpl<Type, Long> implements TypeDao {

    @Autowired
    public TypeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Type.class);
    }

}
