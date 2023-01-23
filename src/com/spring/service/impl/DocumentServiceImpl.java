/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.service.impl;

import com.spring.dao.interfaces.DocumentDao;
import com.spring.entity.Document;
import com.spring.service.interfaces.DocumentService;
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
public class DocumentServiceImpl  implements DocumentService {

    @Autowired
    DocumentDao d;

    @Override
    public List<Document> getBy(Map<String, Object> critiria) {
       return d.getBy(critiria);
    }

   

    @Override
    public Document get(Long id) {
       return d.get(id);
    }

    @Override
    public List<Document> getAll() {
       return d.getAll();
    }

   
    @Override
    public void createOrUpdate(Document persistentObject) throws Exception {
        d.createOrUpdate(persistentObject);
    }

    @Override
    public void delete(Document persistentObject) {
        d.delete(persistentObject);
    }

  

   
}
