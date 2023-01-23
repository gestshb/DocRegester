package com.spring.dao.interfaces;

import com.spring.entity.Procedure;
import java.util.List;


public interface ProcedureDao extends GenericDao<Procedure, Long> {
    List<Procedure> findByName(String action);


}
