package com.spring.service.interfaces;

import com.spring.entity.Rendezvous;
import java.util.List;
import java.util.Map;



public interface RendezvousService extends GenericService<Rendezvous, Long> {
    List<Rendezvous> getBy(Map<String, Object> critiria);
}
