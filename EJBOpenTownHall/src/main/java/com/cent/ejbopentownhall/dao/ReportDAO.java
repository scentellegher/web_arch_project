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
import com.cent.ejbopentownhall.entity.User;
import com.cent.ejbopentownhall.entity.Picture;
import java.util.ArrayList;
import java.util.Collection;
import com.cent.ejbopentownhall.entity.Comment;
import com.cent.ejbopentownhall.entity.Report;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.transaction.UserTransaction;

/**
 *
 * @author cent
 */
public class ReportDAO implements Serializable {

    public ReportDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Integer create(Report report) throws RollbackFailureException, Exception {
        if (report.getPictureCollection() == null) {
            report.setPictureCollection(new ArrayList<Picture>());
        }
        if (report.getCommentCollection() == null) {
            report.setCommentCollection(new ArrayList<Comment>());
        }
        Integer result=-1; //return -1 if there is an error
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User idUser = report.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                report.setIdUser(idUser);
            }
            User idWorker = report.getIdWorker();
            if (idWorker != null) {
                idWorker = em.getReference(idWorker.getClass(), idWorker.getId());
                report.setIdWorker(idWorker);
            }
            Collection<Picture> attachedPictureCollection = new ArrayList<Picture>();
            for (Picture pictureCollectionPictureToAttach : report.getPictureCollection()) {
                pictureCollectionPictureToAttach = em.getReference(pictureCollectionPictureToAttach.getClass(), pictureCollectionPictureToAttach.getId());
                attachedPictureCollection.add(pictureCollectionPictureToAttach);
            }
            report.setPictureCollection(attachedPictureCollection);
            Collection<Comment> attachedCommentCollection = new ArrayList<Comment>();
            for (Comment commentCollectionCommentToAttach : report.getCommentCollection()) {
                commentCollectionCommentToAttach = em.getReference(commentCollectionCommentToAttach.getClass(), commentCollectionCommentToAttach.getId());
                attachedCommentCollection.add(commentCollectionCommentToAttach);
            }
            report.setCommentCollection(attachedCommentCollection);
            em.persist(report);
            em.flush();
            result = report.getId();
            if (idUser != null) {
                idUser.getReportCollection().add(report);
                idUser = em.merge(idUser);
            }
            if (idWorker != null) {
                idWorker.getReportCollection().add(report);
                idWorker = em.merge(idWorker);
            }
            for (Picture pictureCollectionPicture : report.getPictureCollection()) {
                Report oldReportIdOfPictureCollectionPicture = pictureCollectionPicture.getReportId();
                pictureCollectionPicture.setReportId(report);
                pictureCollectionPicture = em.merge(pictureCollectionPicture);
                if (oldReportIdOfPictureCollectionPicture != null) {
                    oldReportIdOfPictureCollectionPicture.getPictureCollection().remove(pictureCollectionPicture);
                    oldReportIdOfPictureCollectionPicture = em.merge(oldReportIdOfPictureCollectionPicture);
                }
            }
            for (Comment commentCollectionComment : report.getCommentCollection()) {
                Report oldReportIdOfCommentCollectionComment = commentCollectionComment.getReportId();
                commentCollectionComment.setReportId(report);
                commentCollectionComment = em.merge(commentCollectionComment);
                if (oldReportIdOfCommentCollectionComment != null) {
                    oldReportIdOfCommentCollectionComment.getCommentCollection().remove(commentCollectionComment);
                    oldReportIdOfCommentCollectionComment = em.merge(oldReportIdOfCommentCollectionComment);
                }
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

    public void edit(Report report) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Report persistentReport = em.find(Report.class, report.getId());
            User idUserOld = persistentReport.getIdUser();
            User idUserNew = report.getIdUser();
            User idWorkerOld = persistentReport.getIdWorker();
            User idWorkerNew = report.getIdWorker();
            
            Collection<Comment> commentCollectionOld = persistentReport.getCommentCollection();            
            Collection<Comment> commentCollectionNew = null;
            if(report.getCommentCollection()==null){
                commentCollectionNew = new ArrayList<Comment>();
            } else {
                commentCollectionNew = report.getCommentCollection();
            }
            Collection<Picture> pictureCollectionOld = persistentReport.getPictureCollection();
            Collection<Picture> pictureCollectionNew = null;
            if(report.getPictureCollection()==null){
                pictureCollectionNew = new ArrayList<Picture>();
            } else {
                pictureCollectionNew = report.getPictureCollection();
            }

            List<String> illegalOrphanMessages = null;
            for (Picture pictureCollectionOldPicture : pictureCollectionOld) {
                if (!pictureCollectionNew.contains(pictureCollectionOldPicture)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Picture " + pictureCollectionOldPicture + " since its reportId field is not nullable.");
                }
            }
            for (Comment commentCollectionOldComment : commentCollectionOld) {
                if (!commentCollectionNew.contains(commentCollectionOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentCollectionOldComment + " since its reportId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                report.setIdUser(idUserNew);
            }
            if (idWorkerNew != null) {
                idWorkerNew = em.getReference(idWorkerNew.getClass(), idWorkerNew.getId());
                report.setIdWorker(idWorkerNew);
            }
            Collection<Picture> attachedPictureCollectionNew = new ArrayList<Picture>();
            for (Picture pictureCollectionNewPictureToAttach : pictureCollectionNew) {
                pictureCollectionNewPictureToAttach = em.getReference(pictureCollectionNewPictureToAttach.getClass(), pictureCollectionNewPictureToAttach.getId());
                attachedPictureCollectionNew.add(pictureCollectionNewPictureToAttach);
            }
            pictureCollectionNew = attachedPictureCollectionNew;
            report.setPictureCollection(pictureCollectionNew);
            Collection<Comment> attachedCommentCollectionNew = new ArrayList<Comment>();
            for (Comment commentCollectionNewCommentToAttach : commentCollectionNew) {
                commentCollectionNewCommentToAttach = em.getReference(commentCollectionNewCommentToAttach.getClass(), commentCollectionNewCommentToAttach.getId());
                attachedCommentCollectionNew.add(commentCollectionNewCommentToAttach);
            }
            commentCollectionNew = attachedCommentCollectionNew;
            report.setCommentCollection(commentCollectionNew);
            report = em.merge(report);
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getReportCollection().remove(report);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getReportCollection().add(report);
                idUserNew = em.merge(idUserNew);
            }
            if (idWorkerOld != null && !idWorkerOld.equals(idWorkerNew)) {
                idWorkerOld.getReportCollection().remove(report);
                idWorkerOld = em.merge(idWorkerOld);
            }
            if (idWorkerNew != null && !idWorkerNew.equals(idWorkerOld)) {
                idWorkerNew.getReportCollection().add(report);
                idWorkerNew = em.merge(idWorkerNew);
            }
            for (Picture pictureCollectionNewPicture : pictureCollectionNew) {
                if (!pictureCollectionOld.contains(pictureCollectionNewPicture)) {
                    Report oldReportIdOfPictureCollectionNewPicture = pictureCollectionNewPicture.getReportId();
                    pictureCollectionNewPicture.setReportId(report);
                    pictureCollectionNewPicture = em.merge(pictureCollectionNewPicture);
                    if (oldReportIdOfPictureCollectionNewPicture != null && !oldReportIdOfPictureCollectionNewPicture.equals(report)) {
                        oldReportIdOfPictureCollectionNewPicture.getPictureCollection().remove(pictureCollectionNewPicture);
                        oldReportIdOfPictureCollectionNewPicture = em.merge(oldReportIdOfPictureCollectionNewPicture);
                    }
                }
            }
            for (Comment commentCollectionNewComment : commentCollectionNew) {
                if (!commentCollectionOld.contains(commentCollectionNewComment)) {
                    Report oldReportIdOfCommentCollectionNewComment = commentCollectionNewComment.getReportId();
                    commentCollectionNewComment.setReportId(report);
                    commentCollectionNewComment = em.merge(commentCollectionNewComment);
                    if (oldReportIdOfCommentCollectionNewComment != null && !oldReportIdOfCommentCollectionNewComment.equals(report)) {
                        oldReportIdOfCommentCollectionNewComment.getCommentCollection().remove(commentCollectionNewComment);
                        oldReportIdOfCommentCollectionNewComment = em.merge(oldReportIdOfCommentCollectionNewComment);
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
                Integer id = report.getId();
                if (findReport(id) == null) {
                    throw new NonexistentEntityException("The report with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void myEdit(Report report) throws RollbackFailureException, Exception{       
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Report persistentReport = em.find(Report.class, report.getId());
            if(!persistentReport.getTitle().equals(report.getTitle())){
                persistentReport.setTitle(report.getTitle());
            }
            if(!persistentReport.getDescription().equals(report.getDescription())){
                persistentReport.setDescription(report.getDescription());
            }
            if(!persistentReport.getAddress().equals(report.getAddress())){
                persistentReport.setAddress(report.getAddress());
            }
            
            persistentReport = em.merge(persistentReport);
            
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = report.getId();
                if (findReport(id) == null) {
                    throw new NonexistentEntityException("The report with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        
        
    }
    
    public void myEditWorker(Report report) throws RollbackFailureException, Exception{       
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Report persistentReport = em.find(Report.class, report.getId());
            if(persistentReport.getIdWorker()!=report.getIdWorker()){
                persistentReport.setIdWorker(report.getIdWorker());
            }
            if(persistentReport.getStatus()!=report.getStatus()){
                persistentReport.setStatus(report.getStatus());
            }            
            persistentReport = em.merge(persistentReport);
            
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = report.getId();
                if (findReport(id) == null) {
                    throw new NonexistentEntityException("The report with id " + id + " no longer exists.");
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
            Report report;
            try {
                report = em.getReference(Report.class, id);
                report.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The report with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Picture> pictureCollectionOrphanCheck = report.getPictureCollection();
            for (Picture pictureCollectionOrphanCheckPicture : pictureCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Report (" + report + ") cannot be destroyed since the Picture " + pictureCollectionOrphanCheckPicture + " in its pictureCollection field has a non-nullable reportId field.");
            }
            Collection<Comment> commentCollectionOrphanCheck = report.getCommentCollection();
            for (Comment commentCollectionOrphanCheckComment : commentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Report (" + report + ") cannot be destroyed since the Comment " + commentCollectionOrphanCheckComment + " in its commentCollection field has a non-nullable reportId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User idUser = report.getIdUser();
            if (idUser != null) {
                idUser.getReportCollection().remove(report);
                idUser = em.merge(idUser);
            }
            User idWorker = report.getIdWorker();
            if (idWorker != null) {
                idWorker.getReportCollection().remove(report);
                idWorker = em.merge(idWorker);
            }
            em.remove(report);
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

    
    public void myDestroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception{
        Report myReport = findReport(id);
        CommentDAO commentDAO = new CommentDAO(utx, emf);
        for (Comment comment : myReport.getCommentCollection()) {
            commentDAO.destroy(comment.getId());
        }
        PictureDAO pictureDAO = new PictureDAO(utx, emf);
        for (Picture picture : myReport.getPictureCollection()) {
            pictureDAO.destroy(picture.getId());
        }
        destroy(id);
    }
        
    public List<Report> findReportEntities() {
        return findReportEntities(true, -1, -1);
    }

    public List<Report> findReportEntities(int maxResults, int firstResult) {
        return findReportEntities(false, maxResults, firstResult);
    }

    private List<Report> findReportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Report.class));
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
   
    public List<Report> findReportEntitiesSortByDate(int id) {
        return findReportEntitiesSortByDate(true, -1, -1, id, null, null);
    }

    public List<Report> findReportEntitiesSortByDate(int maxResults, int firstResult, int id) {
        return findReportEntitiesSortByDate(false, maxResults, firstResult, id, null, null);
    }
    
    public List<Report> findReportEntitiesSortByDate(int id, String type, String input) {
        return findReportEntitiesSortByDate(true, -1, -1, id, type, input);
    }   

    private List<Report> findReportEntitiesSortByDate(boolean all, int maxResults, int firstResult, int id, String type, String input) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Report> rep= cq.from(Report.class);
            cq.select(rep);
            cq.orderBy(cb.desc(rep.get("timestamp")));
            if(input==null){ // se non sto facendo una ricerca
                if(id != -1) //se ho uno user id allora filtro
                    cq.where(cb.equal(rep.get("idUser").get("id"), id));
                Query q = em.createQuery(cq);
                if (!all) {
                    q.setMaxResults(maxResults);
                    q.setFirstResult(firstResult);
                }
                return q.getResultList();
            } else {
                if(type.equals("title")){
                    Expression<String> path = rep.get("title");
                    cq.where(cb.like(cb.lower(path), "%"+input.toLowerCase()+"%"));
                    Query q = em.createQuery(cq);
                    if (!all) {
                        q.setMaxResults(maxResults);
                        q.setFirstResult(firstResult);
                    }
                    return q.getResultList();
                } else {
                    Expression<String> path = rep.get("address");
                    cq.where(cb.like(cb.lower(path), input.toLowerCase()+"%"));
                    Query q = em.createQuery(cq);
                    if (!all) {
                        q.setMaxResults(maxResults);
                        q.setFirstResult(firstResult);
                    }
                    return q.getResultList();
                }
            }
        } finally {
            em.close();
        }
    }
    
    public Report findReport(Integer id) {
        EntityManager em = getEntityManager();
        try {
            Report res = em.find(Report.class, id);
            return res;
        } finally {
            em.close();
        }
    }

    public int getReportCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Report> rt = cq.from(Report.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
