/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.utils;

import com.cent.ejbopentownhall.ejb.UserDataManagerRemote;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author cent
 */
@FacesValidator("emailValidator")
public class EmailValidator implements Validator {
    
    @EJB(name = "ejb/UserDataManager")
    private UserDataManagerRemote managerRemote;
    
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
			"(\\.[A-Za-z]{2,})$";
    
    private Pattern pattern;
    private Matcher matcher;
    
    public EmailValidator(){
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        System.out.println("validate");
        System.out.println("email: "+o.toString());
        matcher = pattern.matcher(o.toString());
        if(!matcher.matches()){   
            FacesMessage msg = new FacesMessage("E-mail validation failed.", "Invalid E-mail format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);   
        }
        if(!managerRemote.checkMail(o.toString())){
            FacesMessage msg = new FacesMessage("E-mail validation failed.", "E-mail already taken!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);   
        }
            
    }
}
