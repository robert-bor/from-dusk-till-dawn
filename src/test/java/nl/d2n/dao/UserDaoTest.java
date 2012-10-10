package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.Citizen;
import nl.d2n.model.City;
import nl.d2n.model.User;
import nl.d2n.model.UserKey;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.UserCitizenConverter;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class UserDaoTest extends SpringContextTestCase {

    @Resource
    private UserDao userDao;

    @Resource
    private ClassCreator classCreator;

    @Test
    public void construct() {
        User user = new User();
        user.setKey(new UserKey("abcdef"));
        user.setName("Rulesy");
        user.setBanned(true);
        user.setSecure(true);
        user.setShunned((true));
        userDao.save(user);
        user = userDao.find(user.getKey());
        assertEquals("abcdef", user.getKey().getKey());
        assertEquals("Rulesy", user.getName());
        assertEquals(true, user.isBanned());
        assertEquals(true, user.isSecure());
        assertEquals(true, user.isShunned());
    }

    @Test
    public void findByName() {
        classCreator.createUser("Alpha", new UserKey("cafebabe"), 11);
        assertNotNull(userDao.findByGameId(11));
    }

    @Test
    public void testFindCityOfUser() {
        City city = createCity();
        User user = createUser(city);
        assertEquals(true, userDao.isUserInCity(user.getId(), city.getId()));
    }

    @Test
    public void testFindCityOfUserNoCityExists() {
        User user = createUser(null);
        assertEquals(false, userDao.isUserInCity(user.getId(), 13499));
    }

    @Test
    public void testGetCityIdOfUser() {
        User user = createUser(createCity());
        assertEquals((Integer)13449, userDao.getCityId(new UserKey("cafebabe")));
    }

    @Test
    public void findDeadUsersInTown() {
        City city = classCreator.createCity(111, "City 1");
        User user1 = classCreator.createUser("User 1", new UserKey("ABCDEF"), 11, true, false, false, city);
        User user2 = classCreator.createUser("User 2", new UserKey("GHIJKL"), 22, true, false, false, city);
        User user3 = classCreator.createUser("User 3", new UserKey("MNOPQR"), 33, true, false, false, city);
        User user4 = classCreator.createUser("User 4", new UserKey("STUVWX"), 44, true, false, false, city);
        User user5 = classCreator.createUser("User 5", new UserKey("YZABCD"), 55, true, false, false, classCreator.createCity(222, "City 2"));
        List<Citizen> aliveCitizens = UserCitizenConverter.convertUsersToCitizens(new User[] { user1, user2, user4 });
        List<User> users = userDao.findDeadUsersInTown(111, aliveCitizens);
        assertEquals(1, users.size());
    }

    @Test
    public void testFindUsersNotYetRegisteredToSameTown() {
        User user1 = classCreator.createUser("User 1", new UserKey("ABCDEF"), 11, true, false, false, classCreator.createCity(111, "City 1"));
        User user2 = classCreator.createUser("User 2", new UserKey("GHIJKL"), 22, true, false, false, classCreator.createCity(222, "City 2"));
        User user3 = classCreator.createUser("User 3", new UserKey("MNOPQR"), 33, true, false, false, classCreator.createCity(333, "City 3"));
        User user4 = classCreator.createUser("User 4", new UserKey("STUVWX"), 44, true, false, false, null);
        List<Citizen> citizens = UserCitizenConverter.convertUsersToCitizens(new User[] { user1, user2, user3, user4 });
        List<User> users = userDao.findUsersNotYetRegisteredToSameTown(111, citizens);
        assertEquals(3, users.size());
    }

    @Test
    public void testFindUsersInCity() {
        City cityOne = classCreator.createCity(111, "City 1");
        classCreator.createUser("User 1", new UserKey("ABCDEF"), true, false, false, cityOne);
        classCreator.createUser("User 2", new UserKey("ABCDEG"), true, false, false, classCreator.createCity(222, "City 2"));
        classCreator.createUser("User 3", new UserKey("ABCDEH"), true, false, false, cityOne);
        classCreator.createUser("User 4", new UserKey("ABCDEI"), true, false, false, cityOne);
        classCreator.createUser("User 5", new UserKey("ABCDEJ"), true, false, false, classCreator.createCity(333, "City 3"));
        assertEquals(3, userDao.findUsersInCity(111).size());
    }

    protected User createUser(City city) {
        User user = new User();
        user.setName("Bob");
        user.setKey(new UserKey("cafebabe"));
        user.setCity(city);
        userDao.save(user);
        return userDao.find(new UserKey("cafebabe"));
    }

    protected City createCity() {
        return classCreator.createCity(13449, "Cliffe of Tricks");
    }
}
