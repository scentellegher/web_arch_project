package com.cent.libopentownhall.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author cent
 */
public class ReportDTO implements Serializable{
    
    private Integer id;
    private String title;
    private String description;
    private String descriptionShort;
    private Date timestamp;
    private String address;
    private String coordinates;
    private Integer status;
    private UserDTO user;
    private UserDTO worker;
    private Collection<PictureDTO> pictureCollection;
    private Collection<CommentDTO> commentCollection;

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * @return the descriptionShort
     */
    public String getDescriptionShort() {
        return descriptionShort;
    }

    /**
     * @param descriptionShort the descriptionShort to set
     */
    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    /**
     * @return the coordinates
     */
    public String getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return the worker
     */
    public UserDTO getWorker() {
        return worker;
    }

    /**
     * @param worker the worker to set
     */
    public void setWorker(UserDTO worker) {
        this.worker = worker;
    }

    /**
     * @return the pictureCollection
     */
    public Collection<PictureDTO> getPictureCollection() {
        return pictureCollection;
    }

    /**
     * @param pictureCollection the pictureCollection to set
     */
    public void setPictureCollection(Collection<PictureDTO> pictureCollection) {
        this.pictureCollection = pictureCollection;
    }

    /**
     * @return the commentCollection
     */
    public Collection<CommentDTO> getCommentCollection() {
        return commentCollection;
    }

    /**
     * @param commentCollection the commentCollection to set
     */
    public void setCommentCollection(Collection<CommentDTO> commentCollection) {
        this.commentCollection = commentCollection;
    }

}
