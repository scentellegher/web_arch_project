/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.libopentownhall.dto.ReportDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

/**
 *
 * @author cent
 */
@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
public class Watchlist implements WatchlistRemote {
    
    private List<ReportDTO> watchlist;
    
    @PostConstruct
    private void init(){
       watchlist = new ArrayList<ReportDTO>();
    }


    @Override
    public void followReport(ReportDTO report) {
        watchlist.add(report);
    }

    @Override
    public void unfollowReport(ReportDTO report) {
        for (int i=0; i< watchlist.size(); i++) {
            if(watchlist.get(i).getId().equals(report.getId()))
                watchlist.remove(i);
        }
        watchlist.remove(report);
    }

    @Override
    public List<ReportDTO> getList() {
        return watchlist;
    }

}
