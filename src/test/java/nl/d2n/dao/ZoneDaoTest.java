package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.model.builder.ZoneBuilder;
import nl.d2n.util.ClassCreator;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class ZoneDaoTest extends SpringContextTestCase {

    @Resource
    private ClassCreator classCreator;

    @Resource
    private CityDao cityDao;

    @Resource
    private ZoneDao zoneDao;

    @Test
    public void saveZone() {

        City city = classCreator.createCity(13449, "Cliffe of Tricks");

        Zone zone = saveZone(createZone(city, -10, 5, true, true, true, "Rolly's Rotor House", CampingTopology.L3_MINIMAL, 19));

        assertEquals(-10, zone.getX());
        assertEquals(5, zone.getY());
        assertEquals(true ,zone.isZoneDepleted());
        assertEquals(true ,zone.isBuildingDepleted());
        assertEquals(true, zone.isBluePrintRetrieved());
        assertEquals("Rolly's Rotor House", zone.getScoutPeek());
        assertEquals(CampingTopology.L3_MINIMAL, zone.getCampingTopology());
        assertEquals(19, zone.getZombies());
    }

    @Test
    public void findZoneThatDoesNotExist() {
        assertNull(zoneDao.findZone(1,1,1));
    }

    @Test
    public void multipleZonesForCity() {
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        saveZone(createZone(city, -10, 5, true, true, false, "Rolly's Rotor House", CampingTopology.L7_IDEAL, 19));
        saveZone(createZone(city, -11, 5, true, true, false, "Citizen's Tent", CampingTopology.L6_TOP_HIDEOUTS, 23));
        saveZone(createZone(city, -12, 5, true, true, true, "Shaweer Kebab's Oven", CampingTopology.L1_SUICIDE, 4));
        city = classCreator.createCity(13450, "Some other town");
        saveZone(createZone(city, -1, 1, true, false, true, "", CampingTopology.L5_FEW_HIDEOUTS, 0));

        List zones = zoneDao.findZones(13449);
        assertEquals(3, zones.size());
    }

    @Test
    public void overwriteAction() {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"));
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        Zone zone = saveZone(createZone(city, -10, 5, true, true, false, "Rolly's Rotor House", CampingTopology.L4_HIDING_PLACE, 19));
        zone.addLastUserAction(classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:32:20").getDateTime()));
        zone.addLastUserAction(classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:32:21").getDateTime()));
        zone = saveZone(zone);
        assertEquals(1, zone.getLastUserActions().size());
    }

    @Test
    public void saveZoneWithActions() {
        User user = classCreator.createUser("Bob", new UserKey("cafebabe"));
        City city = classCreator.createCity(13449, "Cliffe of Tricks");
        Zone zone = saveZone(createZone(city, -10, 5, true, true, false, "Rolly's Rotor House", CampingTopology.L3_MINIMAL, 19));
        zone.addLastUserAction(classCreator.createUserAction(city, user, zone, UpdateAction.UPDATE_ZONE, new GameClock("2011-01-01 18:32:20").getDateTime()));
        zone.addLastUserAction(classCreator.createUserAction(city, user, zone, UpdateAction.SAVE_ZOMBIES, new GameClock("2011-01-01 18:32:20").getDateTime()));
        zone.addLastUserAction(classCreator.createUserAction(city, user, zone, UpdateAction.SAVE_PEEK, new GameClock("2011-01-01 18:32:20").getDateTime()));
        zone = saveZone(zone);
        assertEquals(3, zone.getLastUserActions().size());
    }

    protected Zone saveZone(Zone zone) {
        zoneDao.saveZone(zone);
        return zoneDao.findZone(zone.getCity().getId(), zone.getX(), zone.getY());
    }

    public static Zone createZone(City city, int x, int y, boolean zoneDepleted, boolean buildingDepleted,
                                  boolean bluePrintRetrieved, String scoutPeek, CampingTopology campingTopology,
                                  int zombies) {
        return new ZoneBuilder()
                .setX(x)
                .setY(y)
                .setZoneDepleted(zoneDepleted)
                .setBuildingDepleted(buildingDepleted)
                .setBluePrintRetrieved(bluePrintRetrieved)
                .setScoutPeek(scoutPeek)
                .setCampingTopology(campingTopology)
                .setZombies(zombies)
                .setCity(city)
                .toZone();
    }
}
