package bo.net.tigo.dao;

import bo.net.tigo.model.Contact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class ContactDaoImpl implements ContactDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Contact contact) {
        Session session = sessionFactory.getCurrentSession();
        session.save(contact);
    }

    @Override
    public void update(Contact contact) {
        Session session = sessionFactory.getCurrentSession();
        session.update(contact);
    }

    @Override
    public Contact findOne(Long contactId) {
        Session session = sessionFactory.getCurrentSession();
        return (Contact)session.get(Contact.class, contactId);

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contact> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Contact").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contact> findEnabled(boolean enabled) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Contact contact "+
                                   "where contact.enabled = :enabled")
                                    .setParameter("enabled",enabled)
                                    .list();
    }

}
