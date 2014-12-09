/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.UserDataManagerRemote;
import com.cent.libopentownhall.dto.UserDTO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author cent
 */
@Named
@RequestScoped
public class RegistrationView {
    
    @EJB(name = "ejb/UserDataManager")
    private UserDataManagerRemote managerRemote;
    
    @Inject
    LoginView loginView;
    
    private String name;
    private String surname;
    private String email;
    private String password1;
    private String password2;
    private Date birth;
    private String address;
    private String phone;
    private String code;

    public String doRegistration(){
        //create a DTO
        UserDTO u = new UserDTO();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        u.setPassword(md5(password1));
        u.setBirthdate(birth);
        u.setAddress(address);
        u.setPhone(phone);
        u.setFiscalCode(code);
        u.setRole("user"); //setto manualmente
        
        //add user
        boolean result = managerRemote.addUser(u);
        if(result){
            //login
            loginView.setEmail(email);
            loginView.setPassword(password1);

            return loginView.doLogin();
        } else {
            return "home.xhtml?faces-redirect=true";
        }
    }
    
    public String addWorker(){
        System.out.println("ADD WORKER");
        //create a DTO
        UserDTO u = new UserDTO();
        u.setName(name);
        System.out.println(u.getName());
        u.setSurname(surname);
        System.out.println(u.getSurname());
        u.setEmail(email);
        System.out.println(u.getEmail());
        u.setPassword(md5(password1));
        System.out.println(u.getPassword());
        u.setBirthdate(birth);
        System.out.println(u.getBirthdate());
        u.setAddress(address);
        System.out.println(u.getAddress());
        u.setPhone(phone);
        System.out.println(u.getPhone());
        u.setFiscalCode(code);
        System.out.println(u.getFiscalCode());
        u.setRole("worker"); 
        System.out.println(u.getRole());
        
        //add worker
        System.out.println(managerRemote.addUser(u));
        return "admin.xhtml?faces-redirect=true";
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

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
     * @return the password1
     */
    public String getPassword1() {
        return password1;
    }

    /**
     * @param password1 the password1 to set
     */
    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    /**
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * @param password2 the password2 to set
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * @return the birth
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * @param birth the birth to set
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
