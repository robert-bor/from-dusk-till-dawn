package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.result.LastUpdateByUser;
import nl.d2n.dao.result.UserUpdateCount;
import nl.d2n.dao.result.UserWithProfile;
import nl.d2n.model.*;
import nl.d2n.model.builder.CityBuilder;
import nl.d2n.model.UpdateAction;
import nl.d2n.util.ClassCreator;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class UserActionDaoTest extends SpringContextTestCase {

    @Resource
    private UserActionDao userActionDao;

    @Resource
    private ClassCreator classCreator;

    @Test
    public void construct() {
        Date date = new GameClock("2011-01-01 18:32:20").getDateTime();
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"));
        Zone zone = classCreator.createZone(city, 14, 7);
        classCreator.createUserAction(city, user, zone, UpdateAction.ADD_BLUEPRINT, date);

        UserAction action = userActionDao.findActions(city.getId(), user.getId()).get(0);

        assertEquals(UpdateAction.ADD_BLUEPRINT, action.getAction());
        assertEquals(date, action.getUpdated());
    }

    @Test
    public void findLastUpdateTimes() {
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"));
        Zone zone = classCreator.createZone(city, 14, 7);

        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:32:20").getDateTime());
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:34:20").getDateTime());
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:38:20").getDateTime());

        user = classCreator.createUser("Henk", new UserKey("0xSOMEOTHERKEY"));
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:39:20").getDateTime());

        List<LastUpdateByUser> results = userActionDao.findLastUpdateTimes(city.getId());
        assertEquals(2, results.size());
        assertEquals(new GameClock("2011-01-01 18:38:20").getDateTime(), results.get(0).getUpdated());
        assertEquals(new GameClock("2011-01-01 18:39:20").getDateTime(), results.get(1).getUpdated());
    }

    @Test
    public void countUserActions() {
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"), true, false, false, city);
        Zone zone = classCreator.createZone(city, 14, 7);

        classCreator.createUserAction(city, user, zone, UpdateAction.REPLENISH_BUILDING, new GameClock("2011-01-06 13:17:20").getDateTime()); // TOO OLD
        classCreator.createUserAction(city, user, zone, UpdateAction.READ_MAP,           new GameClock("2011-01-07 14:44:20").getDateTime()); // < 48 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.DEPLETE_ZONE,       new GameClock("2011-01-08 00:32:20").getDateTime()); // < 48 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.DEPLETE_BUILDING,   new GameClock("2011-01-08 06:32:20").getDateTime()); // < 48 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE,        new GameClock("2011-01-08 12:32:20").getDateTime()); // < 12 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE,        new GameClock("2011-01-08 18:34:20").getDateTime()); // < 12 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE,        new GameClock("2011-01-08 18:38:20").getDateTime()); // < 12 hrs

        user = classCreator.createUser("Henk", new UserKey("0xSOMEOTHERKEY"), true, false, false, city);
        classCreator.createUserAction(city, user, zone, UpdateAction.READ_MAP,           new GameClock("2011-01-08 18:33:20").getDateTime()); // < 12 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE,        new GameClock("2011-01-08 18:39:20").getDateTime()); // < 12 hrs
        classCreator.createUserAction(city, user, zone, UpdateAction.READ_MAP,           new GameClock("2011-01-08 18:40:20").getDateTime()); // < 12 hrs

        GameClock now = new GameClock("2011-01-08 18:40:22");

        List<UserUpdateCount> veryRecentChanges = userActionDao.findRecentChanges(city.getId(), now.getHoursAgo(12), now.getDateTime());
        assertEquals(2, veryRecentChanges.size());
        assertEquals((Integer)3, veryRecentChanges.get(0).getWrites());
        assertEquals((Integer)0, veryRecentChanges.get(0).getReads());
        assertEquals((Integer)1, veryRecentChanges.get(1).getWrites());
        assertEquals((Integer)2, veryRecentChanges.get(1).getReads());

        List<UserUpdateCount> recentChanges = userActionDao.findRecentChanges(city.getId(), now.getHoursAgo(48), now.getHoursAgo(12));
        assertEquals(1, recentChanges.size());
        assertEquals((Integer)2, recentChanges.get(0).getWrites());
        assertEquals((Integer)1, recentChanges.get(0).getReads());
    }

    @Test
    public void getUserIds() {
        classCreator.createUser("Ap", new UserKey("cafebabe"), 11);
        classCreator.createUser("Bert", new UserKey("cafebabe"), 22);
        classCreator.createUser("Corne", new UserKey("cafebabe"), 33);
        List<Citizen> citizens = new ArrayList<Citizen>();
        citizens.add(CitizenBuilder.createCitizen("Bert", 22));
        citizens.add(CitizenBuilder.createCitizen("Corne", 33));
        assertEquals(2, userDao.findUserIds(citizens).size());
    }

}
