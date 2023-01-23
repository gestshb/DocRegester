package com.spring.service.interfaces;

import com.spring.entity.Procedure;
import java.util.List;


public interface ProcedureService extends GenericService<Procedure, Long> {
    List<Procedure> findByName(String action);


}
