/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.dao;

import com.cent.ejbopentownhall.dao.exceptions.IllegalOrphanException;
import com.cent.ejbopentownhall.dao.exceptions.NonexistentEntityException;
import com.cent.ejbopentownhall.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cent.ejbopentownhall.entity.Picture;
import java.util.ArrayList;
import java.util.Collection;
import com.cent.ejbopentownhall.entity.Comment;
import com.cent.ejbopentownhall.entity.Report;
import com.cent.ejbopentownhall.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author cent
 */
public class UserDAO implements Serializable {

    public UserDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws RollbackFailureException, Exception {
        if (user.getPictureCollection() == null) {
            user.setPictureCollection(new ArrayList<Picture>());
        }
        if (user.getCommentCollection() == null) {
            user.setCommentCollection(new ArrayList<Comment>());
        }
        if (user.getReportCollection() == null) {
            user.setReportCollection(new ArrayList<Report>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Picture> attachedPictureCollection = new ArrayList<Picture>();
            for (Picture pictureCollectionPictureToAttach : user.getPictureCollection()) {
                pictureCollectionPictureToAttach = em.getReference(pictureCollectionPictureToAttach.getClass(), pictureCollectionPictureToAttach.getId());
                attachedPictureCollection.add(pictureCollectionPictureToAttach);
            }
            user.setPictureCollection(attachedPictureCollection);
            Collection<Comment> attachedCommentCollection = new ArrayList<Comment>();
            for (Comment commentCollectionCommentToAttach : user.getCommentCollection()) {
                commentCollectionCommentToAttach = em.getReference(commentCollectionCommentToAttach.getClass(), commentCollectionCommentToAttach.getId());
                attachedCommentCollection.add(commentCollectionCommentToAttach);
            }
            user.setCommentCollection(attachedCommentCollection);
            Collection<Report> attachedReportCollection = new ArrayList<Report>();
            for (Report reportCollectionReportToAttach : user.getReportCollection()) {
                reportCollectionReportToAttach = em.getReference(reportCollectionReportToAttach.getClass(), reportCollectionReportToAttach.getId());
                attachedReportCollection.add(reportCollectionReportToAttach);
            }
            user.setReportCollection(attachedReportCollection);
            em.persist(user);
            for (Picture pictureCollectionPicture : user.getPictureCollection()) {
                User oldUserIdOfPictureCollectionPicture = pictureCollectionPicture.getUserId();
                pictureCollectionPicture.setUserId(user);
                pictureCollectionPicture = em.merge(pictureCollectionPicture);
                if (oldUserIdOfPictureCollectionPicture != null) {
                    oldUserIdOfPictureCollectionPicture.getPictureCollection().remove(pictureCollectionPicture);
                    oldUserIdOfPictureCollectionPicture = em.merge(oldUserIdOfPictureCollectionPicture);
                }
            }
            for (Comment commentCollectionComment : user.getCommentCollection()) {
                User oldUserIdOfCommentCollectionComment = commentCollectionComment.getUserId();
                commentCollectionComment.setUserId(user);
                commentCollectionComment = em.merge(commentCollectionComment);
                if (oldUserIdOfCommentCollectionComment != null) {
                    oldUserIdOfCommentCollectionComment.getCommentCollection().remove(commentCollectionComment);
                    oldUserIdOfCommentCollectionComment = em.merge(oldUserIdOfCommentCollectionComment);
                }
            }
            for (Report reportCollectionReport : user.getReportCollection()) {
                User oldIdUserOfReportCollectionReport = reportCollectionReport.getIdUser();
                reportCollectionReport.setIdUser(user);
                reportCollectionReport = em.merge(reportCollectionReport);
                if (oldIdUserOfReportCollectionReport != null) {
                    oldIdUserOfReportCollectionReport.getReportCollection().remove(reportCollectionReport);
                    oldIdUserOfReportCollectionReport = em.merge(oldIdUserOfReportCollectionReport);
                }
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

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User persistentUser = em.find(User.class, user.getId());
            Collection<Picture> pictureCollectionOld = persistentUser.getPictureCollection();
            Collection<Picture> pictureCollectionNew = user.getPictureCollection();
            Collection<Comment> commentCollectionOld = persistentUser.getCommentCollection();
            Collection<Comment> commentCollectionNew = user.getCommentCollection();
            Collection<Report> reportCollectionOld = persistentUser.getReportCollection();
            Collection<Report> reportCollectionNew = user.getReportCollection();
            List<String> illegalOrphanMessages = null;
            for (Picture pictureCollectionOldPicture : pictureCollectionOld) {
                if (!pictureCollectionNew.contains(pictureCollectionOldPicture)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Picture " + pictureCollectionOldPicture + " since its userId field is not nullable.");
                }
            }
            for (Comment commentCollectionOldComment : commentCollectionOld) {
                if (!commentCollectionNew.contains(commentCollectionOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentCollectionOldComment + " since its userId field is not nullable.");
                }
            }
            for (Report reportCollectionOldReport : reportCollectionOld) {
                if (!reportCollectionNew.contains(reportCollectionOldReport)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Report " + reportCollectionOldReport + " since its idUser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Picture> attachedPictureCollectionNew = new ArrayList<Picture>();
            for (Picture pictureCollectionNewPictureToAttach : pictureCollectionNew) {
                pictureCollectionNewPictureToAttach = em.getReference(pictureCollectionNewPictureToAttach.getClass(), pictureCollectionNewPictureToAttach.getId());
                attachedPictureCollectionNew.add(pictureCollectionNewPictureToAttach);
            }
            pictureCollectionNew = attachedPictureCollectionNew;
            user.setPictureCollection(pictureCollectionNew);
            Collection<Comment> attachedCommentCollectionNew = new ArrayList<Comment>();
            for (Comment commentCollectionNewCommentToAttach : commentCollectionNew) {
                commentCollectionNewCommentToAttach = em.getReference(commentCollectionNewCommentToAttach.getClass(), commentCollectionNewCommentToAttach.getId());
                attachedCommentCollectionNew.add(commentCollectionNewCommentToAttach);
            }
            commentCollectionNew = attachedCommentCollectionNew;
            user.setCommentCollection(commentCollectionNew);
            Collection<Report> attachedReportCollectionNew = new ArrayList<Report>();
            for (Report reportCollectionNewReportToAttach : reportCollectionNew) {
                reportCollectionNewReportToAttach = em.getReference(reportCollectionNewReportToAttach.getClass(), reportCollectionNewReportToAttach.getId());
                attachedReportCollectionNew.add(reportCollectionNewReportToAttach);
            }
            reportCollectionNew = attachedReportCollectionNew;
            user.setReportCollection(reportCollectionNew);
            user = em.merge(user);
            for (Picture pictureCollectionNewPicture : pictureCollectionNew) {
                if (!pictureCollectionOld.contains(pictureCollectionNewPicture)) {
                    User oldUserIdOfPictureCollectionNewPicture = pictureCollectionNewPicture.getUserId();
                    pictureCollectionNewPicture.setUserId(user);
                    pictureCollectionNewPicture = em.merge(pictureCollectionNewPicture);
                    if (oldUserIdOfPictureCollectionNewPicture != null && !oldUserIdOfPictureCollectionNewPicture.equals(user)) {
                        oldUserIdOfPictureCollectionNewPicture.getPictureCollection().remove(pictureCollectionNewPicture);
                        oldUserIdOfPictureCollectionNewPicture = em.merge(oldUserIdOfPictureCollectionNewPicture);
                    }
                }
            }
            for (Comment commentCollectionNewComment : commentCollectionNew) {
                if (!commentCollectionOld.contains(commentCollectionNewComment)) {
                    User oldUserIdOfCommentCollectionNewComment = commentCollectionNewComment.getUserId();
                    commentCollectionNewComment.setUserId(user);
                    commentCollectionNewComment = em.merge(commentCollectionNewComment);
                    if (oldUserIdOfCommentCollectionNewComment != null && !oldUserIdOfCommentCollectionNewComment.equals(user)) {
                        oldUserIdOfCommentCollectionNewComment.getCommentCollection().remove(commentCollectionNewComment);
                        oldUserIdOfCommentCollectionNewComment = em.merge(oldUserIdOfCommentCollectionNewComment);
                    }
                }
            }
            for (Report reportCollectionNewReport : reportCollectionNew) {
                if (!reportCollectionOld.contains(reportCollectionNewReport)) {
                    User oldIdUserOfReportCollectionNewReport = reportCollectionNewReport.getIdUser();
                    reportCollectionNewReport.setIdUser(user);
                    reportCollectionNewReport = em.merge(reportCollectionNewReport);
                    if (oldIdUserOfReportCollectionNewReport != null && !oldIdUserOfReportCollectionNewReport.equals(user)) {
                        oldIdUserOfReportCollectionNewReport.getReportCollection().remove(reportCollectionNewReport);
                        oldIdUserOfReportCollectionNewReport = em.merge(oldIdUserOfReportCollectionNewReport);
                    }
                }
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
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Picture> pictureCollectionOrphanCheck = user.getPictureCollection();
            for (Picture pictureCollectionOrphanCheckPicture : pictureCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Picture " + pictureCollectionOrphanCheckPicture + " in its pictureCollection field has a non-nullable userId field.");
            }
            Collection<Comment> commentCollectionOrphanCheck = user.getCommentCollection();
            for (Comment commentCollectionOrphanCheckComment : commentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Comment " + commentCollectionOrphanCheckComment + " in its commentCollection field has a non-nullable userId field.");
            }
            Collection<Report> reportCollectionOrphanCheck = user.getReportCollection();
            for (Report reportCollectionOrphanCheckReport : reportCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Report " + reportCollectionOrphanCheckReport + " in its reportCollection field has a non-nullable idUser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
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
    
    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }
    
    public User findUserByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(rt);
            cq.where(em.getCriteriaBuilder().equal(rt.get("email"), email));
            Query q = em.createQuery(cq);
            return ((User) q.getSingleResult());
        } catch(NoResultException e){
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<User> findWorkers() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(rt);
            cq.where(em.getCriteriaBuilder().equal(rt.get("role"), "worker"));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } catch(NoResultException e){
            return null;
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
