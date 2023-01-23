package com.spring.service.interfaces;

import com.spring.entity.Action;
import java.util.List;


public interface ActionService extends GenericService<Action, String>  {
    List<Action> findByName(String action);
    


}
