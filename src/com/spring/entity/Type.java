/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.entity;

import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author ahmed
 */
@Entity
public class Type extends AbstractPersistentObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idType;
    @Column(unique = true, nullable = false)
    private String type;

    public Type() {
    }

    public Type(String type) {
        this.type = type;
    }

    public long getIdType() {
        return idType;
    }

    public void setIdType(long idType) {
        this.idType = idType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (idType == null) {
            return super.equals(o);
        } else if (o instanceof Type) {
            return Objects.equals(idType, ((Type) o).idType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (idType == null) {
            return super.hashCode();
        }
        return idType.hashCode();
    }
    @Override
    public String toString() {
        return type;
    }

}
