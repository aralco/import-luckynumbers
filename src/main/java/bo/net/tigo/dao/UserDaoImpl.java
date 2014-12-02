package bo.net.tigo.dao;

import bo.net.tigo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    @Override
    public User findOne(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        return (User)session.get(User.class, userId);

    }

    @Override
    @SuppressWarnings("unchecked")
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return (User)session.createQuery("from User where username=:username")
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User").list();

    }
}
