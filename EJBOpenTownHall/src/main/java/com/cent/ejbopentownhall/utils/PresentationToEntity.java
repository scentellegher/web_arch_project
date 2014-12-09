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
public class PresentationToEntity {
    
    public static User convertUserDTO(UserDTO user){
        if(user==null){
            return null;
        }
        User u = new User();
        u.setId(user.getId());
        u.setName(user.getName());
        u.setSurname(user.getSurname());
        u.setBirthdate(user.getBirthdate());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setAddress(user.getAddress());
        u.setPhone(user.getPhone());
        u.setFiscalCode(user.getFiscalCode());
        u.setRole(user.getRole());
        return u;
    }
    
    public static Report convertReportDTO(ReportDTO dto){
        if(dto==null)
            return null;
        Report r = new Report();
        r.setId(dto.getId());
        r.setTitle(dto.getTitle());
        r.setDescription(dto.getDescription());
        r.setTimestamp(dto.getTimestamp());
        r.setAddress(dto.getAddress());
        r.setCoordinates(dto.getCoordinates());
        r.setStatus(dto.getStatus());
        r.setIdUser(convertUserDTO(dto.getUser()));
        r.setIdWorker(convertUserDTO(dto.getWorker()));
        r.setPictureCollection(convertPicturesDTO(dto.getPictureCollection()));
        return r;
    }
    
    public static Picture convertPictureDTO(PictureDTO dto){
        if(dto==null)
            return null;
        Picture p = new Picture();
        p.setId(dto.getId());
        p.setPath(dto.getPath());
        p.setReportId(convertReportDTO(dto.getReportId()));
        p.setUserId(convertUserDTO(dto.getUserId()));
        return p;
    }
    
    public static List<Picture> convertPicturesDTO(Collection<PictureDTO> pl){
        if(pl==null)
            return null;
        List<Picture> list = new ArrayList<Picture>();
        for (PictureDTO p : pl) {
            list.add(convertPictureDTO(p));
        }
        return list;
    }
    
    public static Comment convertCommentDTO(CommentDTO dto){
        if(dto==null)
            return null;
        Comment c = new Comment();
        c.setId(dto.getId());
        c.setText(dto.getText());
        c.setTimestamp(dto.getTimestamp());
        c.setReportId(convertReportDTO(dto.getReportId()));
        c.setUserId(convertUserDTO(dto.getUserId()));
        return c;
    }
    
    public static List<Comment> convertCommentsDTO(Collection<CommentDTO> cl){
        if(cl==null)
            return null;
        List<Comment> list = new ArrayList<Comment>();
        for (CommentDTO c : cl) {
            list.add(convertCommentDTO(c));
        }
        return list;
    }
    
    
}
