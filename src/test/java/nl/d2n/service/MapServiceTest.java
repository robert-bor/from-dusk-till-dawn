package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.*;
import nl.d2n.model.*;
import nl.d2n.model.builder.CityBuilder;
import nl.d2n.reader.D2NXmlCache;
import nl.d2n.reader.D2NXmlFile;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.D2NXmlReaderFromFile;
import nl.d2n.service.actions.*;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

public class MapServiceTest extends SpringContextTestCase {

    private static final Integer CITY_ID = 13449;

    @Autowired
    private MapService mapService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Resource
    private ZoneDao zoneDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private InsideBuildingDao insideBuildingDao;

    @Resource
    private ClassCreator classCreator;

    @Autowired
    private D2NXmlReader d2NXmlReader;

    @Autowired
    private UserActionDao userActionDao;

    @Autowired
    private UniqueInsideBuildingManager uniqueInsideBuildingManager;

    @Autowired
    private D2NXmlCache cache;

    @Autowired
    private D2NXmlFile fileAccessor;

    @Autowired
    ApplicationContext applicationContext;

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

    @After
    public void resetCache() {
        cache.setD2NXmlFile(fileAccessor);
    }

    @Before
    public void setServiceRight() {
        mapService.setD2nXmlReader(d2NXmlReader);
    }

    @Test
    public void updateItemsManually() throws Exception {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
        // First do the secure update to set the items
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
        List<Zone> zones = zoneDao.findZones(14390);
        assertEquals(2, zones.get(0).getItems().size());
        // Then do a manual update
        applicationContext.getBean(ZoneActionItems.class).setItems(Item.convertKeysToItems(new String[] { "38-1", "184-1", "28-1" })).execute(new UserKey("cafebabe"), -4, 9, 5);
        zones = zoneDao.findZones(14390);
        assertEquals(3, zones.get(0).getItems().size());
        // First do the secure update to set the items
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
        zones = zoneDao.findZones(14390);
        assertEquals(2, zones.get(0).getItems().size());
    }

    // BUG - Items which have been set for a zone where wiped out
    @Test
    public void itemsAreKeptAfterManualUpdate() throws Exception {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
        // First do the secure update to set the items
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
        List<Item> items = itemDao.findAllItems();
        List<Zone> zones = zoneDao.findZones(14390);
        assertEquals(2, zones.get(0).getItems().size());
        // Then do a manual update
        applicationContext.getBean(ZoneActionDepleteZone.class).execute(new UserKey("cafebabe"), -4, 9, 5);
        zones = zoneDao.findZones(14390);
        assertEquals(2, zones.get(0).getItems().size());
    }

    @Test
    public void saveCampingTopology() throws Exception {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
        applicationContext.getBean(ZoneActionCampingTopology.class).setCampingTopology(CampingTopology.L5_FEW_HIDEOUTS).execute(new UserKey("cafebabe"), -4, 9, 5);
        List<Zone> zones = zoneDao.findZones(14390);
        assertEquals(CampingTopology.L5_FEW_HIDEOUTS, zones.get(0).getCampingTopology());
    }

    // BUG - A town in chaos mode no longer has the MyZone element filled in for a citizen
    @Test
    public void townInChaosMode() throws Exception  {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("chaos-mode.xml"));
        try {
            applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
            fail("Excepting an error to be thrown because the town is in chaos");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CHAOS_MODE, err.getError());
        }
    }

    // BUG - when a zone has not yet been stored in the database and it gets updated, its log message
    //       does not show the zone ID
    @Test
    public void updateZoneForTheFirstTime() throws Exception  {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
        // Update 1st time
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
        UserAction action = userActionDao.findAllActions().get(0);
        assertEquals(UpdateAction.UPDATE_ZONE, action.getAction());
        assertEquals(1, zoneDao.findZones(14390).size());
    }

    @Test
    public void youAreDead() throws Exception  {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("dead-in-town-without-chaos.xml"));
        try {
            applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.YOU_ARE_DEAD, err.getError());
        }
    }

    @Test
    public void youAreInAHardTown() throws Exception  {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("hard-town.xml"));
        try {
            applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe"));
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.HARD_TOWN, err.getError());
        }
    }

    @SuppressWarnings({"NullableProblems"})
    @Test
    public void getXY() throws Exception {
        cache.setD2NXmlFile(new D2NXmlFile(D2NXmlReaderFromFile.INPUT_PATH));
        City city = classCreator.createCity(13449, "Highlanders Inc");
        zoneDao.saveZone(ZoneDaoTest.createZone(city, 0, 1, true, false, false, null, CampingTopology.L3_MINIMAL, 42));
        Zone zone = mapService.getZone(13449, 0, 1);
        assertEquals(42, zone.getZombies());
    }

    @SuppressWarnings({"NullableProblems"})
    @Test
    public void getZones() throws Exception {
        cache.setD2NXmlFile(new D2NXmlFile(D2NXmlReaderFromFile.INPUT_PATH));
        City city = classCreator.createCity(13449, "Highlanders Inc");
        Map<Integer, Map<Integer, Zone>> zones = mapService.getZones(13449);
        assertEquals(27, zones.keySet().size());
    }

    @Test
    public void getBuildingsWithStatus() throws JAXBException {
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1042, "Upgraded Map", false, 1050, "item_electro", "Lorem ipsum", "http://somewiki.com/upgraded_map", false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1050, "Watchtower", false, null, "item_tagger", "Lorem ipsum", null, false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1062, "Portal Lock", true, null, "small_door_closed", "Lorem ipsum", null, false, false));
        uniqueInsideBuildingManager.refresh();
        classCreator.createInsideBuilding(classCreator.createCity(1666, "Cliffe of tricks"), 1042, InsideBuildingStatus.AVAILABLE);
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"0\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
                "<building name=\"Watchtower\" temporary=\"0\" id=\"1050\" img=\"item_tagger\"></building>" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        List<InsideBuilding> buildings = mapService.getBuildingsWithStatus(city.getBuildings(), 1666);
        assertInsideBuilding(buildings.get(0), 1050, InsideBuildingStatus.CONSTRUCTED);
        assertInsideBuilding(buildings.get(0).getChildBuildings().get(0), 1042, InsideBuildingStatus.AVAILABLE);
        assertInsideBuilding(buildings.get(1), 1062, InsideBuildingStatus.NOT_AVAILABLE);
    }

    @Test
    public void checkAlwaysAvailableBuildings() throws JAXBException {
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1042, "Upgraded Map", false, null, "item_electro", "Lorem ipsum", "http://somewiki.com/upgraded_map", false, true));
        uniqueInsideBuildingManager.refresh();
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"0\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        List<InsideBuilding> buildings = mapService.getBuildingsWithStatus(city.getBuildings(), 1666);
        assertEquals(1, buildings.size());
        assertEquals(InsideBuildingStatus.AVAILABLE, buildings.get(0).getStatus());
        assertEquals(true, buildings.get(0).isAlwaysAvailable());
    }

    @Test
    public void userNotInGame() throws ApplicationException {
        User user = classCreator.createUser("Heltharion", new UserKey("abcdef"), true, false, false, classCreator.createCity(13449, "Cliffe of tricks"));
        MapService service = new MapService();
        service.setUserService(userService);
        service.setD2nXmlReader(new D2NXmlReader() {
            public InfoWrapper read(final UserKey userKey, final boolean allowReadFromCache) throws ApplicationException {
                throw new ApplicationException(D2NErrorCode.NOT_IN_GAME);
            }
        });
        try {
            service.readInfo(user.getKey(), false);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.NOT_IN_GAME, err.getError());
            assertNull(userDao.getCityId(user.getKey()));
        }
    }

    @Test
    public void userInTown() throws ApplicationException {
        City city = classCreator.createCity(13449, "Cliffe of tricks");
        User user = classCreator.createUser("Heltharion", new UserKey("abcdef"), true, false, false, city);
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("13449.xml"));
        applicationContext.getBean(ZoneActionDepleteZone.class).execute(new UserKey("abcdef"), 1, 1, 0);
        assertTrue("Zone 1/1 must be depleted", zoneDao.findZone(13449, 1, 1).isZoneDepleted());
    }

    @Test
    public void userInTownNotYetKnown() throws ApplicationException {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("13449.xml"));
        applicationContext.getBean(ZoneActionDepleteZone.class).execute(new UserKey("abcdef"), 1, 1, 0);
        assertTrue("Zone 1/1 must be depleted", zoneDao.findZone(13449, 1, 1).isZoneDepleted());
    }

    @Test
    public void userNotInTown() throws ApplicationException {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("13449.xml"));
        try {
            applicationContext.getBean(ZoneActionDepleteZone.class).execute(new UserKey("abcdef"), 1, 1, 0);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.NOT_IN_TOWN, err.getError());
        }
    }

    protected void assertInsideBuilding(InsideBuilding building, int id, InsideBuildingStatus status) {
        assertEquals(id, building.getBuildingId());
        assertEquals(status, building.getStatus());
    }

}
