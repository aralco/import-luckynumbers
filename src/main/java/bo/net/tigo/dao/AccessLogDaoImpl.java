package bo.net.tigo.dao;

import bo.net.tigo.model.AccessLog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class AccessLogDaoImpl implements AccessLogDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(AccessLog accessLog) {
        Session session = sessionFactory.getCurrentSession();
        session.save(accessLog);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AccessLog> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from AccessLog").list();
    }
}
