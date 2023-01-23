/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.ProcedureDao;
import com.spring.entity.Procedure;
import com.spring.service.interfaces.ProcedureService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pc
 */
@Service
@Transactional
public class ProcedureServiceImpl implements ProcedureService {
    
    @Autowired
    ProcedureDao d;

    @Override
    public List<Procedure> findByName(String action) {
       return d.findByName(action);
    }

    @Override
    public Procedure get(Long id) {
       return d.get(id);
    }

    @Override
    public List<Procedure> getAll() {
        return d.getAll();
    }

    @Override
    public void createOrUpdate(Procedure persistentObject) throws Exception {
      d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(Procedure persistentObject) {
        d.delete(persistentObject);
    }


   


}
