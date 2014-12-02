package bo.net.tigo.dao;

import bo.net.tigo.model.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Repository
public class CityDaoImpl implements CityDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(City city) {
        Session session = sessionFactory.getCurrentSession();
        session.save(city);
    }

    @Override
    public void update(City city) {
        Session session = sessionFactory.getCurrentSession();
        session.update(city);
    }

    @Override
    public City findOne(Long cityId) {
        Session session = sessionFactory.getCurrentSession();
        return (City)session.get(City.class, cityId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<City> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from City").list();
    }
}
