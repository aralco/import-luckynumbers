package bo.net.tigo.dao;

import bo.net.tigo.model.Job;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class JobDaoImpl implements JobDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Job job) {
        Session session = sessionFactory.getCurrentSession();
        session.save(job);
    }

    @Override
    public void update(Job job) {
        Session session = sessionFactory.getCurrentSession();
        session.update(job);
    }

    @Override
    public Job findOne(Long jobId) {
        Session session = sessionFactory.getCurrentSession();
        return (Job)session.get(Job.class, jobId);
    }

    @Override
    public void delete(Job job) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(job);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Job> finByOwner(String owner) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Job where owner=:owner")
                .setParameter("owner", owner)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Job> finByState(String state) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Job where state=:state")
                .setParameter("state", state)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Job> finBetweenDates(Date from, Date to) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Job where scheduledDate between :from and :to")
                .setParameter("from", from)
                .setParameter("to", to)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Job> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Job").list();
    }
}
