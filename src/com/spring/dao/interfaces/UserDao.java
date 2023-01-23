package com.spring.dao.interfaces;

import com.spring.entity.Action;
import com.spring.entity.User;
import java.util.List;
import java.util.Set;


public interface UserDao extends GenericDao<User, Long> {
    List<User> getByLikeName(String number);
    
   

    User getByName(String name);

    Set<Action> getActions(Long iduser);

    void addActiontoUser(Set<Action> action, User user);


}
