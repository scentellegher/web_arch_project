/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.dao;

import com.cent.ejbopentownhall.entity.Comment;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cent.ejbopentownhall.entity.User;
import com.cent.ejbopentownhall.entity.Report;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import com.cent.ejbopentownhall.dao.exceptions.NonexistentEntityException;
import com.cent.ejbopentownhall.dao.exceptions.RollbackFailureException;

/**
 *
 * @author cent
 */
public class CommentDAO implements Serializable {

    public CommentDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Integer create(Comment comment) throws RollbackFailureException, Exception {
        Integer result=-1; //return -1 if there is an error
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User userId = comment.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                comment.setUserId(userId);
            }
            Report reportId = comment.getReportId();
            if (reportId != null) {
                reportId = em.getReference(reportId.getClass(), reportId.getId());
                comment.setReportId(reportId);
            }
            em.persist(comment);
            em.flush();
            result = comment.getId();
            System.out.println("COMMENT ID="+result);
            if (userId != null) {
                userId.getCommentCollection().add(comment);
                userId = em.merge(userId);
            }
            if (reportId != null) {
                reportId.getCommentCollection().add(comment);
                reportId = em.merge(reportId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                result = -1;
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
            return result;
        }
    }

    public void edit(Comment comment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comment persistentComment = em.find(Comment.class, comment.getId());
            User userIdOld = persistentComment.getUserId();
            User userIdNew = comment.getUserId();
            Report reportIdOld = persistentComment.getReportId();
            Report reportIdNew = comment.getReportId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                comment.setUserId(userIdNew);
            }
            if (reportIdNew != null) {
                reportIdNew = em.getReference(reportIdNew.getClass(), reportIdNew.getId());
                comment.setReportId(reportIdNew);
            }
            comment = em.merge(comment);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getCommentCollection().remove(comment);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getCommentCollection().add(comment);
                userIdNew = em.merge(userIdNew);
            }
            if (reportIdOld != null && !reportIdOld.equals(reportIdNew)) {
                reportIdOld.getCommentCollection().remove(comment);
                reportIdOld = em.merge(reportIdOld);
            }
            if (reportIdNew != null && !reportIdNew.equals(reportIdOld)) {
                reportIdNew.getCommentCollection().add(comment);
                reportIdNew = em.merge(reportIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comment.getId();
                if (findComment(id) == null) {
                    throw new NonexistentEntityException("The comment with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comment comment;
            try {
                comment = em.getReference(Comment.class, id);
                comment.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comment with id " + id + " no longer exists.", enfe);
            }
            User userId = comment.getUserId();
            if (userId != null) {
                userId.getCommentCollection().remove(comment);
                userId = em.merge(userId);
            }
            Report reportId = comment.getReportId();
            if (reportId != null) {
                reportId.getCommentCollection().remove(comment);
                reportId = em.merge(reportId);
            }
            em.remove(comment);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comment> findCommentEntities() {
        return findCommentEntities(true, -1, -1);
    }

    public List<Comment> findCommentEntities(int maxResults, int firstResult) {
        return findCommentEntities(false, maxResults, firstResult);
    }

    private List<Comment> findCommentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comment.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Comment findComment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comment.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comment> rt = cq.from(Comment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
