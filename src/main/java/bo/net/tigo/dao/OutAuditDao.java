package bo.net.tigo.dao;

import bo.net.tigo.model.OutAudit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/18/14.
 */
@Repository
public class OutAuditDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(OutAudit outAudit) {
        Session session = sessionFactory.getCurrentSession();
        session.save(outAudit);
    }

    @SuppressWarnings("unchecked")
    public List<String> findRowsByTask(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "select outaudit.row " +
                "from OutAudit outaudit " +
                "where outaudit.taskId = :taskId")
                .setParameter("taskId",taskId)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<OutAudit> findByTask(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "from OutAudit outaudit " +
                "where taskId = :taskId")
                .setParameter("taskId",taskId)
                .list();
    }

    public Long countOutFilesByJob(Long jobId) {
        Session session = sessionFactory.getCurrentSession();
        return (Long)session.createQuery("" +
                "select count(distinct outaudit.fileName) " +
                "from OutAudit outaudit " +
                "where outaudit.jobId = :jobId "+
                "group by outaudit.jobId")
                .setParameter("jobId",jobId)
                .uniqueResult();
    }

}
