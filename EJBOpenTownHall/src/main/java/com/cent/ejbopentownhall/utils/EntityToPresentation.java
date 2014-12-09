/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.utils;

import com.cent.ejbopentownhall.entity.Comment;
import com.cent.ejbopentownhall.entity.Picture;
import com.cent.ejbopentownhall.entity.Report;
import com.cent.ejbopentownhall.entity.User;
import com.cent.libopentownhall.dto.CommentDTO;
import com.cent.libopentownhall.dto.PictureDTO;
import com.cent.libopentownhall.dto.ReportDTO;
import com.cent.libopentownhall.dto.UserDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cent
 */
public class EntityToPresentation {
    
    public static ReportDTO convertReport(Report r){
        if(r==null)
            return null;
        ReportDTO dto = new ReportDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        String desc = r.getDescription();
        dto.setDescription(desc);
        dto.setDescriptionShort(shortenText(desc));
        dto.setTimestamp(r.getTimestamp());
        dto.setAddress(r.getAddress());
        dto.setCoordinates(r.getCoordinates());
        dto.setStatus(r.getStatus());
        dto.setUser(convertUser(r.getIdUser()));
        dto.setWorker(convertUser(r.getIdWorker()));
        dto.setPictureCollection(convertPictures(r.getPictureCollection()));
        dto.setCommentCollection(convertComments(r.getCommentCollection()));
        return dto;
    }
    
    public static List<ReportDTO> convertReports(List<Report> rl){
        if(rl==null)
            return null;
        List<ReportDTO> list = new ArrayList<ReportDTO>();
        for (Report r : rl) {
            list.add(convertReport(r));
        }
        return list;
    }
    
    public static UserDTO convertUser(User u){
        if(u==null)
            return null;
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setSurname(u.getSurname());
        dto.setBirthdate(u.getBirthdate());
        dto.setEmail(u.getEmail());
        dto.setAddress(u.getAddress());
        dto.setPhone(u.getPhone());
        dto.setFiscalCode(u.getFiscalCode());
        dto.setRole(u.getRole());
        
        return dto;
    }
    
    public static List<UserDTO> convertUsers(List<User> ul){
        if(ul==null)
            return null;
        List<UserDTO> list = new ArrayList<UserDTO>();
        for (User u : ul) {
            list.add(convertUser(u));
        }
        return list;
    }
    
    public static CommentDTO convertComment(Comment c){
        if(c==null)
            return null;
        CommentDTO dto = new CommentDTO();
        dto.setId(c.getId());
        dto.setText(c.getText());
        dto.setTimestamp(c.getTimestamp());
        dto.setUserId(convertUser(c.getUserId()));
        
        return dto;
    }
    
    public static List<CommentDTO> convertComments(Collection<Comment> cl){
        if(cl==null)
            return null;
        List<CommentDTO> list = new ArrayList<CommentDTO>();
        for (Comment c : cl) {
            list.add(convertComment(c));
        }
        return list;
    }
    
    public static PictureDTO convertPicture(Picture p){
        if(p==null)
            return null;
        PictureDTO dto = new PictureDTO();
        dto.setId(p.getId());
        dto.setPath(p.getPath());
        return dto;
    }
    
    public static List<PictureDTO> convertPictures(Collection<Picture> pl){
        if(pl==null)
            return null;
        List<PictureDTO> list = new ArrayList<PictureDTO>();
        for (Picture p : pl) {
            list.add(convertPicture(p));
        }
        return list;
    }
    
      private static String shortenText(String description){
          String tmp = description;
          if(description.length()>170){
              tmp = description.substring(0, 170);
              tmp = tmp + " ...";
          }
          return tmp;
    }
    
}
