/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.ReportManagerRemote;
import com.cent.libopentownhall.dto.CommentDTO;
import com.cent.libopentownhall.dto.PictureDTO;
import com.cent.libopentownhall.dto.ReportDTO;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.json.JSONObject;

/**
 *
 * @author cent
 */
@Named
@SessionScoped
public class ReportView implements Serializable{
    
    @EJB(name = "ejb/ReportManager")
    private ReportManagerRemote managerRemote;
    
    private boolean edit=false;
    
    ///////////report.xhtml/////////////
    private ReportDTO currentReport;
    private JSONObject coord;
    
    /////////new_report.xhtml///////////
    private String title;
    private String description;
    private String city;
    private String address;
    private String streetNumber;
    
    ////////////addComment///////////////
    private String commentText;
    
    @Inject
    LoginView loginView;
    
    @Inject
    InputBean inputBean;
    
    public String goNewReport(){
        edit=false;
        //resetto la form
        title = "";
        description ="";
        city = "";
        address = "";
        streetNumber = "";
        return "report.xhtml?faces-redirect=true";
    }
    
    public String addReport(){
        
        //create dto
        ReportDTO report = new ReportDTO();
        report.setTitle(title);
        report.setDescription(description);
        report.setTimestamp(new Date());
        report.setAddress(city.trim()+", "+address.trim()+" "+streetNumber);
        report.setCoordinates(null); // calcolo le coordinate in ReportManager prima di inserirlo
        report.setStatus(-1);// -1=not assigned, 0=assigned, 1=solved
        report.setUser(loginView.getUser());
        report.setWorker(null);
        
        String path="";
        try {
            path = inputBean.uploadFile();
        } catch (IOException ex) {
            System.out.println("OOPS");
        }
        
        Integer reportId = managerRemote.addReport(report);
        PictureDTO pic = new PictureDTO();
        pic.setPath(path);
        //setto l'id e poi lo metto nella picture (sono uguali)
        report.setId(reportId);
        pic.setReportId(report);
        pic.setUserId(loginView.getUser());
        managerRemote.addPicture(pic);
        
        //resetto i campi
        title = "";
        description ="";
        city = "";
        address = "";
        streetNumber = "";
        return "home.xhtml?faces-redirect=true";
    }
    
    public String goReportPage(){
        String rId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("reportId");
        setCurrentReport(managerRemote.getReportById(Integer.parseInt(rId)));

        coord = new JSONObject();
        coord.put("lat", currentReport.getCoordinates().split(", ")[0]);
        coord.put("lng", currentReport.getCoordinates().split(", ")[1]);
        System.out.println("COMMENTS"+currentReport.getCommentCollection());
        return "view_report.xhtml?faces-redirect=true";
    } 
    
    public String goEditReport(){
        //setto edit=true per titolo e btn
        edit=true;
        //aggiorno i campi
        title = currentReport.getTitle();
        description = currentReport.getDescription();
            String[] addressComplete = currentReport.getAddress().split(",");
            String rest = addressComplete[1].trim();
            int size = rest.split(" ").length;
        city = addressComplete[0];
        address = rest.split(" *[a-zA-Z0-9]*$")[0].trim();
        streetNumber = rest.split(" ")[size-1];

        return "report.xhtml?faces-redirect=true";
    }
    
    public String editReport(){
        //create dto
        ReportDTO report = new ReportDTO();
        report.setId(currentReport.getId()); //old value
        report.setTitle(title);        
        report.setDescription(description);
        report.setTimestamp(currentReport.getTimestamp()); //old value
        report.setAddress(city.trim()+", "+address.trim()+" "+streetNumber);
        report.setCoordinates(null); // calcolo le coordinate in ReportManager prima di inserirlo
        report.setStatus(currentReport.getStatus()); // old value
        report.setUser(currentReport.getUser()); // old value
        report.setWorker(currentReport.getWorker()); // old value
        report.setCommentCollection(currentReport.getCommentCollection()); // old value
        report.setPictureCollection(currentReport.getPictureCollection()); // old value
        System.out.println("pic collection size"+report.getPictureCollection().size());
        String path="";
        try {
            path = inputBean.uploadFile();
        } catch (IOException ex) {
            System.out.println("OOPS");
        }
        System.out.println("path="+path);
        PictureDTO pic = null;
        if(path!=null){//se aggiungo l'immagine
            System.out.println("sono entrato");
            pic = new PictureDTO();
            pic.setPath(path);
            //setto l'id e poi lo metto nella picture (sono uguali)
            report.setId(report.getId());
            pic.setReportId(report);
            pic.setUserId(report.getUser());
            managerRemote.addPicture(pic);
        }
        if(managerRemote.editReport(report)){      
            //aggiorno
            currentReport.setTitle(title); 
            currentReport.setDescription(description); 
            currentReport.setAddress(city.trim()+", "+address.trim()+" "+streetNumber);
            currentReport.setCoordinates("46.069744, 11.121386"); //tmp
            System.out.println("pic="+pic);
            if(pic!=null){ //se ho un immagine aggiunta allora aggiorno
                System.out.println("entro aggiorno pic");
                Collection<PictureDTO> list = currentReport.getPictureCollection();
                list.add(pic);
                System.out.println("pic.path="+pic.getPath());
                currentReport.setPictureCollection(list);
            }
            System.out.println("SIZE="+currentReport.getPictureCollection().size());
            return "view_report.xhtml?faces-redirect=true";
        } else {
            return "home.xhtml?faces-redirect=true";
        }
    }
    
    public String deleteReport(int id){
        System.out.println("id report="+id);
        if(managerRemote.deleteReport(id)){
            return "home.xhtml?faces-redirect=true";
        } else {
            return "view_report.xhtml";
        }
        
    }
    
    public String addComment(){
        if(commentText.equals(""))
            return null;
        CommentDTO comment = new CommentDTO();
        comment.setText(commentText);
        comment.setTimestamp(new Date());
        comment.setUserId(loginView.getUser());
        comment.setReportId(currentReport);
        int commId = managerRemote.addComment(comment);
        System.out.println("COMM ID="+commId);
        comment.setId(commId);
        commentText = "";
        //aggiorno la lista
        Collection<CommentDTO> list = currentReport.getCommentCollection();
        list.add(comment);
        currentReport.setCommentCollection(list);
        return null; // resto qua
    }
    
    public String deleteComment(int id){
        managerRemote.deleteComment(id);
        Collection<CommentDTO> com = currentReport.getCommentCollection();
        CommentDTO tmp=null;
        for (CommentDTO c : com) {
            if(c.getId()==id)
                tmp=c;
        }
        if(tmp!=null)
            com.remove(tmp);
        return null;
    }

    /**
     * @return the currentReport
     */
    public ReportDTO getCurrentReport() {
        return currentReport;
    }

    /**
     * @param currentReport the currentReport to set
     */
    public void setCurrentReport(ReportDTO currentReport) {
        this.currentReport = currentReport;
    }
    
    /**
     * @return the coord
     */
    public JSONObject getCoord() {
        return coord;
    }

    /**
     * @param coord the coord to set
     */
    public void setCoord(JSONObject coord) {
        this.coord = coord;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
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
     * @return the streetNumber
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the commentText
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * @param commentText the commentText to set
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     * @return the edit
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * @param edit the edit to set
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
}
