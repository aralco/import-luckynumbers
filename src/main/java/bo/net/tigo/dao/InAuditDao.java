package bo.net.tigo.dao;

import bo.net.tigo.model.InAudit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/18/14.
 */
@Repository
public class InAuditDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(InAudit inAudit) {
        Session session = sessionFactory.getCurrentSession();
        session.save(inAudit);
    }

    @SuppressWarnings("unchecked")
    public List<String> findRowsByTask(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "select inaudit.row " +
                "from InAudit inaudit " +
                "where inaudit.taskId = :taskId")
                .setParameter("taskId",taskId)
                .list();
    }

    public Long countInFilesByJob(Long jobId) {
        Session session = sessionFactory.getCurrentSession();
        return (Long)session.createQuery(
                "select count(distinct inaudit.fileName) " +
                "from InAudit inaudit " +
                "where inaudit.jobId = :jobId "+
                "group by inaudit.jobId")
                .setParameter("jobId",jobId)
                .uniqueResult();
    }

    public Long countInRowsByTask(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return (Long)session.createQuery(
                "select count(*) " +
                "from InAudit inaudit " +
                "where inaudit.taskId = :taskId ")
                .setParameter("taskId",taskId)
                .uniqueResult();
    }

}
