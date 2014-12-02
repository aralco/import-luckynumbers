package bo.net.tigo.dao;

import bo.net.tigo.model.City;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface CityDao {
    public void save(City city);
    public void update(City city);
    public City findOne(Long cityId);
    public List<City> findAll();
}
