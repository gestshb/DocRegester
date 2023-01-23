package com.spring.dao.interfaces;

import com.spring.entity.Rendezvous;
import java.util.List;
import java.util.Map;



public interface RendezvousDao extends GenericDao<Rendezvous, Long> {
    List<Rendezvous> getBy(Map<String, Object> critiria);
}
