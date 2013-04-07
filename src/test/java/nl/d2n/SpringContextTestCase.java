package nl.d2n;

import nl.d2n.dao.*;
import nl.d2n.model.User;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testContext.xml" })
public abstract class SpringContextTestCase {

    @Resource
    protected UserDao userDao;

    @Resource
    protected CityDao cityDao;

    @Resource
    protected UniqueItemDao uniqueItemDao;

    @Resource
    protected UniqueOutsideBuildingDao uniqueOutsideBuildingDao;

    @Resource
    protected UniqueInsideBuildingDao uniqueInsideBuildingDao;

    @Resource
    protected UniqueDistinctionDao uniqueDistinctionDao;

    @Resource
    protected UniqueTitleDao uniqueTitleDao;

    @Resource
    protected DistinctionDao distinctionDao;

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings({"unchecked"})
    @Transactional
    public void resetCitiesOfUsers() {
        List<User> users = entityManager.createQuery("from User u").getResultList();
        for (User user : users) {
            user.setCity(null);
            userDao.save(user);
        }
    }

    @After
    public void clearDatabase() {
        resetCitiesOfUsers();
        cityDao.deleteCities();
        uniqueItemDao.deleteUniqueItems();
        uniqueOutsideBuildingDao.deleteUniqueOutsideBuildings();
        uniqueInsideBuildingDao.deleteUniqueInsideBuildings();
        uniqueDistinctionDao.deleteUniqueDistinctions();
        uniqueTitleDao.deleteUniqueTitles();
        distinctionDao.deleteDistinctions();
        userDao.deleteUsers();
    }

}
