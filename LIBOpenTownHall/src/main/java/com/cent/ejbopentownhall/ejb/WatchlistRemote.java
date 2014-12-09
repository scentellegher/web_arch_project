/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.libopentownhall.dto.ReportDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author cent
 */
@Remote
public interface WatchlistRemote {
    
    public void followReport(ReportDTO report);
    public void unfollowReport(ReportDTO report);
    public List<ReportDTO> getList();
    
}
