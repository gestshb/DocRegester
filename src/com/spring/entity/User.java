/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.entity;

import com.spring.util.AbstractPersistentObject;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

/**
 * @author pc
 */
@Entity
public class User extends AbstractPersistentObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    private String firstname;
    private String lastname;
    private String telephone;
    private String email;
    private String addres;
    private boolean active;
    private boolean sysAdmin;
   

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    private Date creationDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ACTION",
            joinColumns = {
                    @JoinColumn(name = "iduser")},
            inverseJoinColumns = {
                    @JoinColumn(name = "idaction")})
    private Set<Action> actions = new HashSet<Action>();

  
    
    

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
       
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public boolean isSysAdmin() {
        return sysAdmin;
    }

    public void setSysAdmin(boolean sysAdmin) {
        this.sysAdmin = sysAdmin;
    }

    

    

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setIduser(long iduser) {
        this.idUser = iduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    //hashing password
    public void setPassword(String password) {
        if ((password != null) && !(password.equals(""))) {
            try {

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes());
                byte byteData[] = md.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                this.password = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

   

    @Override
    public boolean equals(Object o) {
        if (idUser == null) {
            return super.equals(o);
        } else if (o instanceof User) {
            return Objects.equals(idUser, ((User) o).idUser);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (idUser == null) {
            return super.hashCode();
        }
        return idUser.hashCode();
    }

    @Override
    public String toString() {
        return firstname;
    }

}
