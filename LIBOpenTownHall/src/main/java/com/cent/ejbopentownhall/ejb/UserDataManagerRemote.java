/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.libopentownhall.dto.UserDTO;
import javax.ejb.Remote;

/**
 *
 * @author cent
 */
@Remote
public interface UserDataManagerRemote {
    
    public UserDTO login (String email, String password);
    public boolean checkMail(String email);
    public boolean addUser(UserDTO user);
    public UserDTO findUserById(Integer id);
    
}
