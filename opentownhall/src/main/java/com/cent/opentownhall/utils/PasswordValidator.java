/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author cent
 */
@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String pwd2 = (String) o;
        
        Object otherValue;
        String pwd1;
        
        UIComponent otherComponent = fc.getViewRoot().findComponent("j_idt6:password1");
        if( otherComponent == null){
            System.out.println("ERROR");
        } else {
            otherValue = ((UIInput) otherComponent).getValue();
            pwd1 = (String) otherValue;
            if(pwd1!=null){
                System.out.println("PWD1= "+pwd1);
                if(!pwd1.equals(pwd2)){
                    FacesMessage msg = new FacesMessage("password match", "Passwords do not match!");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
            
        }
        
    }
    
}
