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

/**
 * @author pc
 */
@Entity
public class Rendezvous extends AbstractPersistentObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRendezvous;
    @ManyToOne
    @JoinColumn(name = "idDocument")
    private Document document;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Rendezvous() {
    }

    @Override
    public boolean equals(Object o) {
        if (idRendezvous == null) {
            return super.equals(o);
        } else if (o instanceof Rendezvous) {
            return Objects.equals(idRendezvous, ((Rendezvous) o).idRendezvous);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (idRendezvous == null) {
            return super.hashCode();
        }
        return idRendezvous.hashCode();
    }

  

    public Long getIdRendezvous() {
        return idRendezvous;
    }

    public void setIdRendezvous(Long idRendezvous) {
        this.idRendezvous = idRendezvous;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

 
}
