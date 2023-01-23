package com.spring.dao.interfaces;



import com.spring.entity.Document;
import java.util.List;
import java.util.Map;


public interface DocumentDao extends GenericDao<Document, Long> {

    List<Document> getBy(Map<String, Object> critiria);
   
 


}
