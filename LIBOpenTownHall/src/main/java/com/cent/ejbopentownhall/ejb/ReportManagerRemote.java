/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.ejb;

import com.cent.libopentownhall.dto.CommentDTO;
import com.cent.libopentownhall.dto.PictureDTO;
import com.cent.libopentownhall.dto.ReportDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author cent
 */
@Remote
public interface ReportManagerRemote {
    
    public List<ReportDTO> getReportsList(int id, String type, String input);
    public ReportDTO getReportById(int id);
    public boolean deleteReport(int id);
    public Integer addReport(ReportDTO report);
    public boolean editReport(ReportDTO report);
    public boolean addPicture(PictureDTO picture);
    public Integer addComment(CommentDTO comment);
    public boolean deleteComment(int id);
    
}
