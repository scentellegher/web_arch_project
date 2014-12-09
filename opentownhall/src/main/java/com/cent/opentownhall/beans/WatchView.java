/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import com.cent.ejbopentownhall.ejb.WatchlistRemote;
import com.cent.libopentownhall.dto.ReportDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author cent
 */
@Named
@SessionScoped
public class WatchView implements Serializable{
    
    @EJB(name = "ejb/Watchlist")
    private WatchlistRemote managerRemote;
    
    private List<ReportDTO> watchlist;
    
    @PostConstruct
    public void init(){
        watchlist = new ArrayList<ReportDTO>();
    }

    
    public String add(ReportDTO r){        
        if(!included(r)){
            managerRemote.followReport(r);
            watchlist.add(r);//aggiorno
        }
        return null;
    }
    
    public String remove(ReportDTO r){
        managerRemote.unfollowReport(r);
        watchlist = managerRemote.getList();
        return null;
    }
    
    /**
     * @return the watchlist
     */
    public List<ReportDTO> getWatchlist() {
        watchlist = managerRemote.getList();
        if(watchlist==null)
            return new ArrayList<ReportDTO>();
        else
            return watchlist;
    }

    /**
     * @param watchlist the watchlist to set
     */
    public void setWatchlist(List<ReportDTO> watchlist) {
        this.watchlist = watchlist;
    }
    
    public boolean included(ReportDTO r){
        boolean contained=false;
        if(watchlist!=null){
            for (ReportDTO report : watchlist) {
                if(report.getId().equals(r.getId()))
                    contained=true;
            }
        }
        System.out.println("contained="+contained);
        return contained;
    }
    
}
