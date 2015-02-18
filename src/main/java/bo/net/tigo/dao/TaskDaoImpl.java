package bo.net.tigo.dao;

import bo.net.tigo.model.Job;
import bo.net.tigo.model.Status;
import bo.net.tigo.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class TaskDaoImpl implements TaskDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.save(task);
    }

    @Override
    public void update(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.update(task);
    }

    @Override
    public Task findOne(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return (Task)session.get(Task.class, taskId);
    }

    @Override
    public void delete(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(task);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Task").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findByJob(Job job) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Task where job = :job").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findScheduledTasksInRange(Date currentDate) {
        Session session = sessionFactory.getCurrentSession();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -60);
        Date pastDate = calendar.getTime();

        return session.createQuery("from Task " +
                "where status = :scheduled " +
                "and executionDate between :pastDate " +
                "and :currentDate")
                .setParameter("scheduled", Status.SCHEDULED.name())
                .setParameter("pastDate", pastDate)
                .setParameter("currentDate", currentDate).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findByStatus(Status status) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Task " +
                "where status = :status")
                .setParameter("status", status.name())
                .list();
    }

    public Task findByFileName(String filename) {
        filename=filename+".in";
        Session session = sessionFactory.getCurrentSession();
        return (Task)session.createQuery("from Task where urlin = :filename")
                .setParameter("filename",filename)
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findBetweenDates(Date fromDate, Date toDate) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Task " +
                "where executionDate between :fromDate " +
                "and :toDate")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate).list();

    }
}
