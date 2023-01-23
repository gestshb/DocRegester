package com.spring.dao.interfaces;

import com.spring.entity.Action;
import java.util.List;


public interface ActionDao extends GenericDao<Action, String> {
    List<Action> findByName(String action);


}
