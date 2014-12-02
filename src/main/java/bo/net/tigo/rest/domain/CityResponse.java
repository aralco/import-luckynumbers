package bo.net.tigo.rest.domain;

import bo.net.tigo.model.City;

import java.util.List;

/**
 * Created by aralco on 11/14/14.
 */
public class CityResponse {

    private List<City> cities;

    public CityResponse(List<City> cities) {
        this.cities = cities;
    }

    public CityResponse() {
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
