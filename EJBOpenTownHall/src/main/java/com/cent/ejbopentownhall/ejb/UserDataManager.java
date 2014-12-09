/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.ejbopentownhall.dao.UserDAO;
import com.cent.ejbopentownhall.entity.User;
import com.cent.ejbopentownhall.utils.EntityToPresentation;
import com.cent.ejbopentownhall.utils.PresentationToEntity;
import com.cent.libopentownhall.dto.UserDTO;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cent
 */
@Stateless
@TransactionManagement( TransactionManagementType.BEAN )
public class UserDataManager implements UserDataManagerRemote {

    @PersistenceContext(unitName = "com.cent_EJBOpenTownHall_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Resource 
    private EJBContext context;
    
    @Override
    public UserDTO login(String email, String password) {
        if(email == null || password == null){
            return null;
        } else {
            UserDAO dao = new UserDAO(context.getUserTransaction(), em.getEntityManagerFactory());
            User u = dao.findUserByEmail(email);
            if(u == null){ //wrong email
                return null;
            } else { //email exist check pwd
                if(u.getPassword().equals(password)){
                    UserDTO user = EntityToPresentation.convertUser(u);
                    return user; //email and password correct!
                } else {
                    return null; // wrong password
                }            
            }
        }

    }

    @Override
    public boolean checkMail(String email) {
        UserDAO dao = new UserDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        User u = dao.findUserByEmail(email);
        if(u == null)
            return true;
        else
            return false;
    }

    @Override
    public boolean addUser(UserDTO user) {
        try {
            UserDAO dao = new UserDAO(context.getUserTransaction(), em.getEntityManagerFactory());
            User u = PresentationToEntity.convertUserDTO(user);
            if(u!=null)
                dao.create(u);
        } catch (Exception ex) {
            System.out.println("ERROR ADD USER");
            return false;
        }
        return true;
    }

    @Override
    public UserDTO findUserById(Integer id) {
        UserDAO dao = new UserDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        return EntityToPresentation.convertUser(dao.findUser(id));
    }

}
