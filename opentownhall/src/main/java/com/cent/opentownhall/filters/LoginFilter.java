/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.opentownhall.filters;

import com.cent.opentownhall.beans.LoginView;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author cent
 */
@WebFilter(urlPatterns = {"/home.xhtml","/profile.xhtml","/report.xhtml", "/view_report.xhtml", "/watchlist.xhtml"})
public class LoginFilter implements Filter {
 
    @Inject
    private LoginView loginView;
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String contextPath = ((HttpServletRequest)request).getContextPath();
        if (loginView == null || !loginView.isLoggedIn()) {    
            ((HttpServletResponse)response).sendRedirect(contextPath + "/index.xhtml");
        }else{
            if(loginView.getUser().getRole().equals("admin")){
                if(((HttpServletRequest)request).getServletPath().equals("/home.xhtml") ||
                        ((HttpServletRequest)request).getServletPath().equals("/report.xhtml") ||
                        ((HttpServletRequest)request).getServletPath().equals("/watchlist.xhtml")){
                    ((HttpServletResponse)response).sendRedirect(contextPath + "/admin.xhtml");
                }
            } else if (loginView.getUser().getRole().equals("worker")){
                if(((HttpServletRequest)request).getServletPath().equals("/home.xhtml") ||
                        ((HttpServletRequest)request).getServletPath().equals("/report.xhtml") ||
                        ((HttpServletRequest)request).getServletPath().equals("/watchlist.xhtml")){
                    ((HttpServletResponse)response).sendRedirect(contextPath + "/worker.xhtml");
                }
            }
        }
        chain.doFilter(request, response);
             
    }
 
    public void init(FilterConfig config) throws ServletException {
        // Nothing to do here!
    }
 
    public void destroy() {
        // Nothing to do here!
    }  
     
}
