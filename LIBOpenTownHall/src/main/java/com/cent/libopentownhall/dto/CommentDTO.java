package com.cent.libopentownhall.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cent
 */
public class CommentDTO implements Serializable {
    private Integer id;
    private String text;
    private Date timestamp;
    private UserDTO userId;
    private ReportDTO reportId;

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
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
