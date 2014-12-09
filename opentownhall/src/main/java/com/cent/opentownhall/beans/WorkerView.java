/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.UserDataManagerRemote;
import com.cent.ejbopentownhall.ejb.WorkerManagerRemote;
import com.cent.libopentownhall.dto.ReportDTO;
import com.cent.libopentownhall.dto.UserDTO;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author cent
 */
@Named
@SessionScoped
public class WorkerView implements Serializable{
    
    @EJB(name = "ejb/WorkerManager")
    private WorkerManagerRemote managerRemote;
    
    @EJB(name = "ejb/UserDataManager")
    private UserDataManagerRemote userRemote;
    
    private int worker;
    private List<UserDTO> workersList;

    @Inject
    ReportView reportView;
    
    public String goViewReport(){
        
        ReportDTO report = reportView.getCurrentReport();
        UserDTO w = userRemote.findUserById(worker);
        if(w!=null){
            report.setWorker(w);
            report.setStatus(0);
            managerRemote.assignWorker(report);
        }
        return null;
    }
    
    public String closeReport(){
        ReportDTO report = reportView.getCurrentReport();
        report.setStatus(1);
        managerRemote.assignWorker(report); //faccio una edit del report!
        return null;
    }
    
    /**
     * @return the worker
     */
    public int getWorker() {
        return worker;
    }

    /**
     * @param worker the worker to set
     */
    public void setWorker(int worker) {
        this.worker = worker;
    }

    /**
     * @return the workersList
     */
    public List<UserDTO> getWorkersList() {
        workersList = managerRemote.getWorkers();
        return workersList;
    }

    /**
     * @param workersList the workersList to set
     */
    public void setWorkersList(List<UserDTO> workersList) {
        this.workersList = workersList;
    }
    
}
