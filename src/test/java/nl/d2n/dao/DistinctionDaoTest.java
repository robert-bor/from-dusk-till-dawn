package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.result.UserWithDistinction;
import nl.d2n.dao.result.UserWithProfile;
import nl.d2n.model.Citizen;
import nl.d2n.model.CitizenBuilder;
import nl.d2n.model.User;
import nl.d2n.model.UserKey;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.UserCitizenConverter;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class DistinctionDaoTest extends SpringContextTestCase {

    @Resource
    private DistinctionDao distinctionDao;

    @Resource
    private ClassCreator classCreator;

    @Test
    public void testDistinctionFetch() {
        User user1 = classCreator.createUser("Bob", new UserKey("cafebabe1"), 11);
        User user2 = classCreator.createUser("Dale", new UserKey("cafebabe2"), 22);
        classCreator.createDistinction(user1, "Knight");
        classCreator.createDistinction(user1, "Shunnee");
        classCreator.createDistinction(user2, "Explorer");

        Map<Integer, UserWithProfile> profiles =
                distinctionDao.findUsersWithDistinctions(1984, userDao.findUserIds(UserCitizenConverter.convertUsersToCitizens(new User[] { user1, user2 })));
        assertEquals(2, profiles.get(11).getDistinctions().size());
        assertEquals(1, profiles.get(22).getDistinctions().size());
    }

    @Test
    public void getTopUsers() {
        User user1 = classCreator.createUser("Bob", new UserKey("cafebabe1"), 11);
        User user2 = classCreator.createUser("Dale", new UserKey("cafebabe2"), 22);
        User user3 = classCreator.createUser("Anne", new UserKey("cafebabe3"), 33);
        classCreator.createDistinction(user1, "Knight", 123);
        classCreator.createDistinction(user2, "Knight", 11);
        classCreator.createDistinction(user3, "Knight", 100);
        List<UserWithDistinction> users = distinctionDao.findTopUsersWithDistinction(1, 2);
        assertEquals("Bob", users.get(0).getUserName());
        assertEquals("Anne", users.get(1).getUserName());
    }

    @Test
    public void testDistinctionFetchForZeroCitizens() {
        try {
            Map<Integer, UserWithProfile> profiles = distinctionDao.findUsersWithDistinctions(1984, new ArrayList<Integer>());
        } catch (Exception err) {
            fail("Should not throw an exception");
        }

    }
}
