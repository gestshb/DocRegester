/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.DocumentDao;
import com.spring.entity.Document;
import com.spring.util.ConverterDate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author pc
 */
@Repository
public class DocumentDaoImpl extends GenericDaoImpl<Document, Long> implements DocumentDao {

    @Autowired
    public DocumentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Document.class);
    }

    @Override
    public List<Document> getBy(Map<String, Object> critiria) {

        Map<String, Object> parm = new HashMap<>();
        StringBuilder query;
        query = new StringBuilder("from Document d where  1=1");

        for (String key : critiria.keySet()) {
            switch (key) {
                case "idDoc":
                    String s = (String) critiria.get(key);
                    if (s.matches("^-?\\d+$")) {
                        query.append(" and d.iddoc =:iddoc ");
                        parm.put("iddoc", Long.parseLong(s));
                    }

                    break;
                case "registrationNumber":
                    query.append("  and d.registrationNumber  like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                 case "orderNumber":
                    query.append("  and d.orderNumber  like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;   

                case "description":
                    query.append("  and d.description like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
             
                case "sender":
                    query.append("  and d.sender like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                case "recipient":
                    query.append("  and d.recipient like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                case "creationDate1":
                    query.append("  and d.creationDate >= :creationDate1");
                    parm.put("creationDate1", ConverterDate.getDate((LocalDate) critiria.get(key)));
                    break;
                case "creationDate2":
                    query.append("  and d.creationDate <= :creationDate1");
                    parm.put("creationDate1", ConverterDate.getDate((LocalDate) critiria.get(key)));
                    break;
                case "type":
                    query.append("  and d.type =:type");
                    parm.put("type", critiria.get(key));
                    break;
                   
            }
        }

        Query q = getCurrentSession().createQuery(query.toString());

        for (String s : parm.keySet()) {
            q.setParameter(s, parm.get(s));
        }
        q.setMaxResults(100);
        return q.list();
    }

   

}
