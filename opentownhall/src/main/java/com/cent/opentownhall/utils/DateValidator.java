/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.utils;

import java.util.Date;
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
@FacesValidator("dateValidator")
public class DateValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        Date dateInput = (Date) o;
        if(dateInput.compareTo(new Date())>=0){
            FacesMessage msg = new FacesMessage("Birthdate validation failed.", "Invalid Birthdate: birthdate in the future!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
    }
    
}
