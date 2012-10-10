package nl.d2n.model.builder;

import nl.d2n.model.City;

public class CityBuilder {

    private City city = new City();

    public CityBuilder setId(Integer id) {
        city.setId(id);
        return this;
    }
    public CityBuilder setName(String name) {
        city.setName(name);
        return this;
    }
    public CityBuilder setHeight(Integer height) {
        city.setHeight(height);
        return this;
    }
    public CityBuilder setWidth(Integer width) {
        city.setWidth(width);
        return this;
    }
    public CityBuilder setLeft(Integer left) {
        city.setLeft(left);
        return this;
    }
    public CityBuilder setRight(Integer right) {
        city.setRight(right);
        return this;
    }
    public CityBuilder setTop(Integer top) {
        city.setTop(top);
        return this;
    }
    public CityBuilder setBottom(Integer bottom) {
        city.setBottom(bottom);
        return this;
    }
    public CityBuilder setHardcore(Boolean hard) {
        city.setHardcore(true);
        return this;
    }
    public City toCity() {
        return city;
    }
}
