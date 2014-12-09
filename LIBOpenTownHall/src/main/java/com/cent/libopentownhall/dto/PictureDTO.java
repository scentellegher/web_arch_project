/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.libopentownhall.dto;

import java.io.Serializable;

/**
 *
 * @author cent
 */
public class PictureDTO implements Serializable{
    private Integer id;
    private String path;
    private UserDTO userId;
    private ReportDTO reportId;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the userId
     */
    public UserDTO getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    /**
     * @return the reportId
     */
    public ReportDTO getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(ReportDTO reportId) {
        this.reportId = reportId;
    }
}
