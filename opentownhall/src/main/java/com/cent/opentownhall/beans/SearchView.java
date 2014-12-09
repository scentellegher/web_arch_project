/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author cent
 */
@Named
@SessionScoped
public class SearchView implements Serializable {
    
    private String searchType = "title";
    private String searchInput;

    /**
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the searchInput
     */
    public String getSearchInput() {
        return searchInput;
    }

    /**
     * @param searchInput the searchInput to set
     */
    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }
    
}
