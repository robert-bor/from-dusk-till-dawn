package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.UserCitizenConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

@SuppressWarnings({"NullableProblems"})
public class UserServiceTest extends SpringContextTestCase {

    public final static boolean SHUNNED     = true;
    public final static boolean NOT_SHUNNED = false;

    public final static boolean SECURE      = true;
    public final static boolean NOT_SECURE  = false;

    public final static boolean NOT_BANNED  = false;

    @Autowired
    private UserService userService;

    @Resource
    private ClassCreator classCreator;

    @Autowired
    private ProfileService profileService;

    @Before
    public void stubProfileService() {
        userService.setProfileService(new ProfileService() {
            public void updateProfile(User user) throws ApplicationException {}
        });
    }

    @After
    public void resetProfileService() {
        userService.setProfileService(profileService);
    }

    @Test
    public void updateUserName() throws ApplicationException {
        classCreator.createUser("Bob", new UserKey("cafebabe"), SECURE, NOT_SHUNNED, NOT_BANNED);
        userService.updateUser(null, new UserKey("cafebabe"), 1984, "Charlie", SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        User user = userDao.find(new UserKey("cafebabe"));
        assertEquals("Charlie", user.getName());
    }

    @Test
    public void updateUserDoesNotExist() throws ApplicationException {
        userService.updateUser(null, new UserKey("cafebabe"), 1984, "Bob", SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        User user = userDao.find(new UserKey("cafebabe"));
        assertEquals("Bob", user.getName());
    }

    @Test
    public void updateInsecureUser() throws ApplicationException {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), SECURE, NOT_SHUNNED, NOT_BANNED);
        userService.updateUser(null, user.getKey(), 1984, user.getName(), NOT_SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        user = userDao.find(user.getKey());
        assertEquals(NOT_SECURE, user.isSecure());
    }

    @Test
    public void updateShunnedUser() throws ApplicationException {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), SECURE, NOT_SHUNNED, NOT_BANNED);
        userService.updateUser(null, user.getKey(), 1984, user.getName(), SECURE, SHUNNED, new ArrayList<Citizen>());
        user = userDao.find(user.getKey());
        assertEquals(SHUNNED, user.isShunned());
    }

    @Test
    public void userChangedKeys() throws ApplicationException {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), 11, SECURE, NOT_SHUNNED, NOT_BANNED, null);
        userService.updateUser(null, new UserKey(user.getKey()+"_NOT_THE_SAME_KEY"), 1984, user.getName(), SECURE, SHUNNED, new ArrayList<Citizen>());
        assertEquals(user.getId(), userDao.findByGameId(11).getId());
    }
    
    @Test
    public void updateUnshunnedUser() throws ApplicationException {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), SECURE, NOT_SHUNNED, NOT_BANNED);
        userService.updateUser(null, user.getKey(), 1984, user.getName(), SECURE, SHUNNED, new ArrayList<Citizen>());
        userService.updateUser(null, user.getKey(), 1984, user.getName(), SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        user = userDao.find(user.getKey());
        assertEquals(NOT_SHUNNED, user.isShunned());
    }

    @Test
    public void updateCity() throws ApplicationException {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), SECURE, NOT_SHUNNED, NOT_BANNED);
        userService.updateUser(classCreator.createCity(1333, "City 1"), user.getKey(), 1984, user.getName(), SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        userService.updateUser(classCreator.createCity(1666, "City 2"), user.getKey(), 1984, user.getName(), SECURE, NOT_SHUNNED, new ArrayList<Citizen>());
        assertEquals(true, userDao.isUserInCity(user.getId(), 1666));
    }

    @Test
    public void removeDeadCitizensFromTown() throws ApplicationException {
        City city = classCreator.createCity(111, "City 1");
        User user1 = classCreator.createUser("User 1", new UserKey("ABCDEF"), 11, true, false, false, city);
        User user2 = classCreator.createUser("User 2", new UserKey("GHIJKL"), 22, true, false, false, city);
        User user3 = classCreator.createUser("User 3", new UserKey("MNOPQR"), 33, true, false, false, city);
        User user4 = classCreator.createUser("User 4", new UserKey("STUVWX"), 44, true, false, false, city);
        User user5 = classCreator.createUser("User 5", new UserKey("YZABCD"), 55, true, false, false, classCreator.createCity(222, "City 2"));
        List<Citizen> aliveCitizens = UserCitizenConverter.convertUsersToCitizens(new User[]{user1, user4});
        userService.removeDeadCitizensFromTown(city, aliveCitizens);
        assertTrue(userDao.isUserInCity(user1.getId(), city.getId()));
        assertFalse(userDao.isUserInCity(user2.getId(), city.getId()));
        assertFalse(userDao.isUserInCity(user3.getId(), city.getId()));
        assertTrue(userDao.isUserInCity(user4.getId(), city.getId()));
    }

    @Test
    public void removeCitizenFromTown() throws ApplicationException {
        User user1 = classCreator.createUser("User 1", new UserKey("abcdef"), true, false, false, classCreator.createCity(111, "City 1"));
        userService.removeCitizenFromTown(new UserKey("abcdef"));
        assertNull(userDao.getCityId(new UserKey("abcdef")));
    }
    
    @Test
    public void updateMultipleProfiles() throws ApplicationException {
        City newCity = classCreator.createCity(444, "City 4");
        User user1 = classCreator.createUser("User 1", new UserKey("ABCDEF"), 11, true, false, false, classCreator.createCity(111, "City 1"));
        User user2 = classCreator.createUser("User 2", new UserKey("GHIJKL"), 22, true, false, false, newCity);
        User user3 = classCreator.createUser("User 3", new UserKey("MNOPQR"), 33, true, false, false, classCreator.createCity(333, "City 3"));
        User user4 = classCreator.createUser("User 4", new UserKey("STUVWX"), 44, true, false, false, null);
        List<Citizen> citizens = UserCitizenConverter.convertUsersToCitizens(new User[]{user1, user2, user3, user4});
        final List<String> namesOfUpdatedUsers = new ArrayList<String>();
        userService.setProfileService(new ProfileService() {
            public void updateProfile(User user) throws ApplicationException {
                namesOfUpdatedUsers.add(user.getName());
            }
        });
        userService.updateUser(newCity, new UserKey("MNOPQR"), 1984, null, true, false, citizens);
        assertEquals(3, namesOfUpdatedUsers.size());
        assertEquals("User 3", namesOfUpdatedUsers.get(0));
        assertTrue("Active user should have been registered to new town", userDao.isUserInCity(user3.getId(), 444)); // Active user
        assertTrue("Bulk updated user should have been registered to new town", userDao.isUserInCity(user1.getId(), 444)); // Bulk updated user
        assertTrue("Bulk updated user should have been registered to new town", userDao.isUserInCity(user4.getId(), 444)); // Bulk updated user
    }
}
