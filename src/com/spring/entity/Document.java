/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.entity;

import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * @author pc
 */
@Entity
public class Document extends AbstractPersistentObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long iddoc;
    private String description;
    private String sender;
    private String recipient;
    private String registrationNumber;
    private String orderNumber;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate;
    private Boolean locked;
    private Boolean treated;

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
    
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "idtype")
    private Type type;
    
    @OneToOne
    @JoinColumn(name = "idDocRelated")
    private Document doc;
    
    @OneToMany(mappedBy = "document")
    @Cascade( {CascadeType.ALL})
    private List<Rendezvous> rendezvous;
    @OneToMany(mappedBy = "document")
    @Cascade( {CascadeType.ALL})
    private List<Procedure> procedures;

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures;
    }
    
    
    

    public List<Rendezvous> getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(List<Rendezvous> rendezvous) {
        this.rendezvous = rendezvous;
    }

  
  

    public Document() {
    }

    public Document(String description) {
        this.description = description;
    }

   


    @Override
    public boolean equals(Object o) {
        if (iddoc == null) {
            return super.equals(o);
        } else if (o instanceof Document) {
            return Objects.equals(iddoc, ((Document) o).iddoc);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (iddoc == null) {
            return super.hashCode();
        }
        return iddoc.hashCode();
    }
    
    @Override
    public String toString(){
    
    return description;
    }

   

    public Long getIddoc() {
        return iddoc;
    }

    public void setIddoc(Long iddoc) {
        this.iddoc = iddoc;
    }

    
    
   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

   
    


   

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getTreated() {
        return treated;
    }

    public void setTreated(Boolean treated) {
        this.treated = treated;
    }

    

    
    
    

}
