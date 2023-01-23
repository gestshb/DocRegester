/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.RendezvousDao;
import com.spring.entity.Rendezvous;
import com.spring.service.interfaces.RendezvousService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pc
 */
@Service
@Transactional
public class RendezvousServiceImpl implements RendezvousService {
    @Autowired
    RendezvousDao d;

    @Override
    public List<Rendezvous> getBy(Map<String, Object> critiria) {
       return d.getBy(critiria);
    }

    @Override
    public Rendezvous get(Long id) {
       return d.get(id);
    }

    @Override
    public List<Rendezvous> getAll() {
       return d.getAll();
    }

    @Override
    public void createOrUpdate(Rendezvous persistentObject) throws Exception {
       d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(Rendezvous persistentObject) {
        d.delete(persistentObject);
    }


  

}
