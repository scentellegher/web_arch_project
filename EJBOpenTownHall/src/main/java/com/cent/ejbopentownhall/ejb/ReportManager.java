/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.ejbopentownhall.dao.CommentDAO;
import com.cent.ejbopentownhall.dao.PictureDAO;
import com.cent.ejbopentownhall.dao.ReportDAO;
import com.cent.ejbopentownhall.utils.EntityToPresentation;
import com.cent.ejbopentownhall.utils.Geocoding;
import com.cent.ejbopentownhall.utils.PresentationToEntity;
import com.cent.libopentownhall.dto.CommentDTO;
import com.cent.libopentownhall.dto.PictureDTO;
import com.cent.libopentownhall.dto.ReportDTO;
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
public class ReportManager implements ReportManagerRemote {

    @PersistenceContext(unitName = "com.cent_EJBOpenTownHall_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Resource 
    private EJBContext context;
    
    @Override
    public List<ReportDTO> getReportsList(int id, String type, String input){
        List<ReportDTO> list = null;
        ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        list = EntityToPresentation.convertReports(dao.findReportEntitiesSortByDate(id, type, input));
        return list;
    }

    @Override
    public ReportDTO getReportById(int id) {
        ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        ReportDTO res = EntityToPresentation.convertReport(dao.findReport(id));
        return res ;
    }

    @Override
    public boolean deleteReport(int id) {
        try {
            ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
            dao.myDestroy(id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public Integer addReport(ReportDTO report) {
        Integer res = -1; // contain the report id generated
        ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        Geocoding geo = new Geocoding();
        String coordinates = geo.findCoordinates(report.getAddress());
        if(coordinates != null){
            report.setCoordinates(coordinates);
        } else {
            report.setCoordinates("46.069744, 11.121386");//coordinate trento
        }
        try {
            res = dao.create(PresentationToEntity.convertReportDTO(report));
        } catch (Exception ex) {
            return -1;
        }
        return res;
    }

    @Override
    public boolean addPicture(PictureDTO picture) {
        PictureDAO dao = new PictureDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        try {
            dao.create(PresentationToEntity.convertPictureDTO(picture));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addComment(CommentDTO comment) {
        CommentDAO dao = new CommentDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        try {
            dao.create(PresentationToEntity.convertCommentDTO(comment));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteComment(int id) {
        CommentDAO dao = new CommentDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        try {
            dao.destroy(id);
        } catch (Exception ex) {
            System.out.println("ERROR");
            return false;
        }
        return true;
    }

    @Override
    public boolean editReport(ReportDTO report) {
        ReportDAO dao = new ReportDAO(context.getUserTransaction(), em.getEntityManagerFactory());
        try {
            dao.myEdit(PresentationToEntity.convertReportDTO(report));
        } catch (Exception ex) {
            System.out.println("ERROR EDIT");
            return false;
        }
        return true;
    }
    
    
}
