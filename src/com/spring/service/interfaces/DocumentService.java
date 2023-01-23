package com.spring.service.interfaces;



import com.spring.entity.Document;
import java.util.List;
import java.util.Map;


public interface DocumentService extends GenericService<Document, Long> {

    List<Document> getBy(Map<String, Object> critiria);
   
 


}
