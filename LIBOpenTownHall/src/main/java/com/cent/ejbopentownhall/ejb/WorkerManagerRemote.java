/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.libopentownhall.dto.ReportDTO;
import com.cent.libopentownhall.dto.UserDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author cent
 */
@Remote
public interface WorkerManagerRemote {
    
    public List<UserDTO> getWorkers();
    public boolean assignWorker(ReportDTO report);
    
}
