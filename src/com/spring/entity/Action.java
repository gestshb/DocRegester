/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.entity;

import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author ahmed
 */
@Entity
public class Action extends AbstractPersistentObject implements Serializable {

   
    private String actionEn;
    private String action;
    @ManyToMany(mappedBy = "actions")
    private Set<User> users = new HashSet<User>();
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAction;
    
    public Integer getIdAction() {
        return idAction;
    }

    public void setIdAction(Integer idAction) {
        this.idAction = idAction;
    }
    
    

    public Action() {
    }

    public Action(String idaction, String action) {
        this.actionEn = idaction;
        this.action = action;
       
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> roles) {
        this.users = roles;
    }

    public String getActionEn() {
        return actionEn;
    }

    public void setActionEn(String actionEn) {
        this.actionEn = actionEn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Action)
            return actionEn.equals(((Action) o).actionEn);
        else
            return false;
    }

    @Override
    public int hashCode() {
        return actionEn.hashCode();
    }

    @Override
    public String toString() {
        return action;
    }

}
