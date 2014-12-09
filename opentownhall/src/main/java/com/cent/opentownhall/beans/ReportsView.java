/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.ReportManagerRemote;
import com.cent.libopentownhall.dto.ReportDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.json.JSONArray;


/**
 *
 * @author cent
 */
@Named
@ViewScoped
public class ReportsView implements Serializable{

    @EJB(name = "ejb/ReportManager")
    private ReportManagerRemote managerRemote;
    
    private String pageTitle;
    private List<ReportDTO> reports;
    private JSONArray list;
    
    @Inject
    SearchView searchView;
    
    @Inject
    LoginView loginView;
    
    @PostConstruct
    public void init(){

        if(loginView.isLoggedIn()){
            //get param
            String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uId");
            String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uName");
            //coordinates collection for markers on gmap
            Collection coordinates;
            int userId;
            String title;
            if(value==null){
                userId = -1;
                title = "Recent Reports";
            } else{
                userId = Integer.parseInt(value);
                title = name +"'s Reports";
            }
            
            String type = null;
            String input = null;
            if(searchView.getSearchInput() != null){
                if(!searchView.getSearchInput().equals("")){ // se non devo fare la ricerca
                    type = searchView.getSearchType();
                    input = searchView.getSearchInput();
                }
            }
            
            List<ReportDTO> tmp = managerRemote.getReportsList(userId, type, input);
            List<ReportDTO> filtered = new ArrayList<ReportDTO>();
            
            //in base all'utente mostro i reports
            if(loginView.getUser().getRole().equals("admin")){
                for (ReportDTO r : tmp) {
                    if(r.getStatus()==-1)
                        filtered.add(r);
                }
                reports = filtered;
            } else if(loginView.getUser().getRole().equals("worker")){ 
                for (ReportDTO r : tmp) {
                    if(r.getWorker()!=null){
                        if(r.getWorker().getId().equals(loginView.getUser().getId())){
                            System.out.println("aggiungo");
                            filtered.add(r);
                        }
                    }
                }
                reports = filtered;
            } else {
                reports = tmp;
            }

            //collect all reports coordinates
            coordinates = new ArrayList();
            for (ReportDTO r : reports) {
                coordinates.add(r.getCoordinates());
            }
            // convert coordinates collection into a JSONArray
            list = new JSONArray(coordinates);
            
            //set home page title
            setPageTitle(title);
            
            //re-initialize search form
            searchView.setSearchType("title");
            searchView.setSearchInput("");
        }
    }
        
    /**
     * @return the reports
     */
    public List<ReportDTO> getReports() {
        return reports;
    }

    /**
     * @param reports the reports to set
     */
    public void setReports(List<ReportDTO> reports) {
        this.reports = reports;
    }

    /**
     * @return the pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @param pageTitle the pageTitle to set
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
   
    public String goHome(){
        return "home.xhtml";
    }

    /**
     * @return the list
     */
    public JSONArray getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(JSONArray list) {
        this.list = list;
    }
       
}
