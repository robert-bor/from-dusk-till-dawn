package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.City;
import nl.d2n.model.builder.CityBuilder;
import nl.d2n.util.ClassCreator;
import org.junit.Test;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class CityDaoTest extends SpringContextTestCase {

    @Resource
    private ClassCreator classCreator;

    @Test
    public void constructorTest() {
        City city = new CityBuilder()
                .setId(13449)
                .setName("Cliffe of Tricks")
                .setWidth(14)
                .setHeight(13)
                .setLeft(-7)
                .setRight(6)
                .setTop(4)
                .setBottom(8)
                .setHardcore(true)
                .toCity();
        cityDao.saveCity(city);
        city = cityDao.findCity(13449);
        assertEquals((Integer)13449, city.getId());
        assertEquals("Cliffe of Tricks", city.getName());
        assertEquals((Integer)14, city.getWidth());
        assertEquals((Integer)13, city.getHeight());
        assertTrue(-7 == city.getLeft());
        assertEquals((Integer)6, city.getRight());
        assertEquals((Integer)4, city.getTop());
        assertEquals((Integer)8, city.getBottom());
        assertTrue(city.isHard());
    }

    @Test
    public void saveCity() {
        City city = createCity();
        cityDao.saveCity(city);
        city = cityDao.findCity(13449);
        assertEquals((Integer)13449, city.getId());
        assertEquals("Cliffe of Tricks", city.getName());
    }

    @Test
    public void saveCityTwice() {
        City city = createCity();
        cityDao.saveCity(city);
        city.setName("Pit of Tricks");
        cityDao.saveCity(city);
        city = cityDao.findCity(13449);
        assertEquals("Pit of Tricks", city.getName());
    }

    @Test
    public void deleteCity() {
        City city = createCity();
        cityDao.saveCity(city);
        cityDao.deleteCity(13449);
        city = cityDao.findCity(city.getId());
        assertNull(city);
    }

    protected City createCity() {
        return classCreator.createCity(13449, "Cliffe of Tricks");
    }
}
