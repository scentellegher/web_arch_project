/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.dao;

import com.cent.ejbopentownhall.entity.Picture;
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
public class PictureDAO implements Serializable {

    public PictureDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Picture picture) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User userId = picture.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                picture.setUserId(userId);
            }
            Report reportId = picture.getReportId();
            if (reportId != null) {
                reportId = em.getReference(reportId.getClass(), reportId.getId());
                picture.setReportId(reportId);
            }
            em.persist(picture);
            if (userId != null) {
                userId.getPictureCollection().add(picture);
                userId = em.merge(userId);
            }
            if (reportId != null) {
                reportId.getPictureCollection().add(picture);
                reportId = em.merge(reportId);
            }
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

    public void edit(Picture picture) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Picture persistentPicture = em.find(Picture.class, picture.getId());
            User userIdOld = persistentPicture.getUserId();
            User userIdNew = picture.getUserId();
            Report reportIdOld = persistentPicture.getReportId();
            Report reportIdNew = picture.getReportId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                picture.setUserId(userIdNew);
            }
            if (reportIdNew != null) {
                reportIdNew = em.getReference(reportIdNew.getClass(), reportIdNew.getId());
                picture.setReportId(reportIdNew);
            }
            picture = em.merge(picture);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPictureCollection().remove(picture);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPictureCollection().add(picture);
                userIdNew = em.merge(userIdNew);
            }
            if (reportIdOld != null && !reportIdOld.equals(reportIdNew)) {
                reportIdOld.getPictureCollection().remove(picture);
                reportIdOld = em.merge(reportIdOld);
            }
            if (reportIdNew != null && !reportIdNew.equals(reportIdOld)) {
                reportIdNew.getPictureCollection().add(picture);
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
                Integer id = picture.getId();
                if (findPicture(id) == null) {
                    throw new NonexistentEntityException("The picture with id " + id + " no longer exists.");
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
            Picture picture;
            try {
                picture = em.getReference(Picture.class, id);
                picture.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The picture with id " + id + " no longer exists.", enfe);
            }
            User userId = picture.getUserId();
            if (userId != null) {
                userId.getPictureCollection().remove(picture);
                userId = em.merge(userId);
            }
            Report reportId = picture.getReportId();
            if (reportId != null) {
                reportId.getPictureCollection().remove(picture);
                reportId = em.merge(reportId);
            }
            em.remove(picture);
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

    public List<Picture> findPictureEntities() {
        return findPictureEntities(true, -1, -1);
    }

    public List<Picture> findPictureEntities(int maxResults, int firstResult) {
        return findPictureEntities(false, maxResults, firstResult);
    }

    private List<Picture> findPictureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Picture.class));
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

    public Picture findPicture(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Picture.class, id);
        } finally {
            em.close();
        }
    }

    public int getPictureCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Picture> rt = cq.from(Picture.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
