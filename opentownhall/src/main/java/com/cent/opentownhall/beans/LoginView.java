/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.UserDataManagerRemote;
import com.cent.libopentownhall.dto.UserDTO;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author cent
 */
@Named
@SessionScoped
public class LoginView implements Serializable{
    
    @EJB(name = "ejb/UserDataManager")
    private UserDataManagerRemote managerRemote;
    
    private boolean loggedIn;
    
    private UserDTO user;
    private String email;
    private String password;
        
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String doLogin(){
        UserDTO user = managerRemote.login(getEmail(), md5(getPassword()));
        if(user!=null){
            loggedIn = true;
            this.setUser(user);            
            return "/home.xhtml?faces-redirect=true";
        } else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Email or Password", "error"); 
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "/index.xhtml";
        }     
   
    }
    
    public String doLogout(){
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
    
    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
      /**
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    private String md5(String plaintext){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
         
    }

}
