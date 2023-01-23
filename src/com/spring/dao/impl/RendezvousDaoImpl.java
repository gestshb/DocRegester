/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.dao.impl;

import com.spring.dao.interfaces.RendezvousDao;
import com.spring.entity.Rendezvous;
import java.util.Date;
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
public class RendezvousDaoImpl extends GenericDaoImpl<Rendezvous, Long> implements RendezvousDao {

    @Autowired
    public RendezvousDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Rendezvous.class);
    }

    
    @Override
    public List<Rendezvous> getBy(Map<String, Object> critiria) {

        Map<String, Object> parm = new HashMap<>();
        StringBuilder query;
        query = new StringBuilder("from Rendezvous d where  1=1");

        for (String key : critiria.keySet()) {
            switch (key) {
                case "idRendezvous":
                    String s = (String) critiria.get(key);
                    if (s.matches("^-?\\d+$")) {
                        query.append(" and d.idRendezvous =:idRendezvous ");
                        parm.put("idRendezvous", Long.parseLong(s));
                    }

                    break;
                case "idDoc":
                    String o = (String) critiria.get(key);
                    if (o.matches("^-?\\d+$")) {
                        query.append(" and d.document.iddoc =:iddoc ");
                        parm.put("iddoc", Long.parseLong(o));
                    }
                    break;
                case "registrationNumber":
                    query.append("  and d.document.registrationNumber  like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                case "orderNumber":
                    query.append("  and d.document.orderNumber  like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;

                case "sender":
                    query.append("  and d.document.sender like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                case "recipient":
                    query.append("  and d.document.recipient like '%");
                    query.append((String) critiria.get(key));
                    query.append("%'");
                    break;
                case "creationDate1":
                    query.append("  and d.date >= :creationDate1");
                    parm.put("creationDate1", (Date) critiria.get(key));
                    break;
                case "creationDate2":
                    query.append("  and d.date <= :creationDate2");
                    parm.put("creationDate2", (Date) critiria.get(key));
                    break;
                case "type":
                    query.append("  and d.document.type =:type");
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
