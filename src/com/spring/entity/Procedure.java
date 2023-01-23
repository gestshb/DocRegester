/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.entity;

import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * @author pc
 */
@Entity
@Table(name ="procedure_")
public class Procedure extends AbstractPersistentObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProcedure;
    private String discription;

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
    @ManyToOne
    @JoinColumn(name = "idDocument")
    private Document document;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateProcedure;

    public Date getDateProcedure() {
        return dateProcedure;
    }

    public void setDateProcedure(Date dateProcedure) {
        this.dateProcedure = dateProcedure;
    }

    public Procedure() {
    }

    @Override
    public boolean equals(Object o) {
        if (idProcedure == null) {
            return super.equals(o);
        } else if (o instanceof Procedure) {
            return Objects.equals(idProcedure, ((Procedure) o).idProcedure);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (idProcedure == null) {
            return super.hashCode();
        }
        return idProcedure.hashCode();
    }
    @Override
    public String toString(){
        return discription;
    }

  

    public Long getIdProcedure() {
        return idProcedure;
    }

    public void setIdProcedure(Long idProcedure) {
        this.idProcedure = idProcedure;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

 
}
