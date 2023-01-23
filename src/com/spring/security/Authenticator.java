package com.spring.security;

import com.spring.service.interfaces.UserService;
import com.spring.entity.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Authenticator implements AuthenticatorInterface {

    @Autowired
    UserService userservice;

    @Override
    public boolean validate(String user, String password) {
        try {

            User userObject = userservice.getByName(user);
            if (userObject != null) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes());
                byte byteData[] = md.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                return userObject.getUsername().equals(user) && userObject.getPassword().equals(sb.toString());
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
}
