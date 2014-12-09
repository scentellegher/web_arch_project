
package com.cent.opentownhall.utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator("FileUploadValidator")
public class FileUploadValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {

		Part part = (Part) value;
                
                if (!"application/octet-stream".equals(part.getContentType()) &&
                        !"image/jpeg".equals(part.getContentType()) &&
                        !"image/jpg".equals(part.getContentType()) &&
                        !"image/png".equals(part.getContentType()) &&
                        !"image/gif".equals(part.getContentType()))  {
                    FacesMessage message = new FacesMessage("Only jpeg, jpg, gif, png are supported!");
                    throw new ValidatorException(message);
                }
	
		if (part.getSize() > 5120000) {
			FacesMessage message = new FacesMessage("Error: File size is too big! 5 MB max allowed!");
			throw new ValidatorException(message);
		}
	}

	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		System.out.println("----- validator partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return "";
	}
}