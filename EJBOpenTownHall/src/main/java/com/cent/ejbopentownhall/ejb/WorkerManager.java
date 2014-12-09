/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.ejbopentownhall.dao.ReportDAO;
import com.cent.ejbopentownhall.dao.UserDAO;
import com.cent.ejbopentownhall.utils.EntityToPresentation;
import com.cent.ejbopentownhall.utils.PresentationToEntity;
import com.cent.libopentownhall.dto.ReportDTO;
import com.cent.libopentownhall.dto.UserDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cent
 */
@Stateless
@TransactionManagement( TransactionManagementType.BEAN )
public class WorkerManager implements WorkerManagerRemote {

    @PersistenceContext(unitName = "com.cent_EJBOpenTownHall_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Resource 
    private EJBContext context;
    
    @Override
    public List<UserDTO> getWorkers() {
        UserDAO dao = new UserDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        List<UserDTO> list = EntityToPresentation.convertUsers(dao.findWorkers());
        return list;
    }

    @Override
    public boolean assignWorker(ReportDTO report) {
        ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        try {
            dao.myEditWorker(PresentationToEntity.convertReportDTO(report));
        } catch (Exception ex) {
            System.out.println("ERR EDIT");
            return false;
        }
        return true;
    }

}
