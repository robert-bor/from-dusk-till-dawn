package nl.d2n.dao;

import nl.d2n.model.City;
import nl.d2n.model.User;
import nl.d2n.model.builder.CityBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CityDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveCity(final City city) {
//        if (city.getId() != null) { evictCity(city.getId()); }
        entityManager.merge(city);
    }

//    @CacheEvict(value = "cities", key="#id")
//    protected void evictCity(Integer cityId) {}

//    @Cacheable(value = "cities", key="#id")
    public City findCity(final Integer id) {
        List results = entityManager
                .createQuery("from City c where c.id = :id")
                .setParameter("id", id)
                .getResultList();
        return (results.size() == 0 ? null : (City)results.get(0));
    }

    @Transactional
    public void deleteCity(final Integer id) {
        deleteCity(findCity(id));
    }

    @Transactional
    protected void deleteCity(final City city) {
        entityManager.remove(city);
    }

    @SuppressWarnings({"unchecked"})
    @Transactional
    public void deleteCities() {
        List<Integer> ids = (List<Integer>)entityManager
                .createQuery("select id from City")
                .getResultList();
        for (Integer id : ids) {
            deleteCity(id);
        }
    }

    @Transactional
    public City findOrCreateCity(final int cityId, final String cityName) {
        City city = findCity(cityId);
        if (city == null) {
            city = new CityBuilder()
                    .setId(cityId)
                    .setName(cityName)
                    .toCity();
            saveCity(city);
        }
        return city;
    }
}
