package nl.d2n.service.actions;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.InsideBuildingDao;
import nl.d2n.model.*;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.D2NXmlReaderFromFile;
import nl.d2n.service.MapService;
import nl.d2n.service.ProfileService;
import nl.d2n.service.UniqueInsideBuildingManager;
import nl.d2n.service.UserService;
import nl.d2n.util.ClassCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class BuildingActionTest extends SpringContextTestCase {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ClassCreator classCreator;
    
    @Autowired
    private UniqueInsideBuildingManager uniqueInsideBuildingManager;
    
    @Autowired
    private InsideBuildingDao insideBuildingDao;
    
    @Autowired
    private MapService mapService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private D2NXmlReader d2NXmlReader;

    @Before
    public void stubProfileService() {
        userService.setProfileService(new ProfileService() {
            public void updateProfile(User user) throws ApplicationException {}
        });
    }

    @Before
    public void createCitizen() {
        classCreator.createUser("Workshop", new UserKey("cafebabe"));
    }

    @Before
    public void createCity() {
        classCreator.createCity(12345, "Temple of the Tedious");
    }

    @Before
    public void createUniqueInsideBuildings() {
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1042, "Upgraded Map", false, 1050, "item_electro", "Lorem ipsum", "http://somewiki.com/upgraded_map", false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1050, "Watchtower", false, null, "item_tagger", "Lorem ipsum", null, false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1062, "Portal Lock", true, null, "small_door_closed", "Lorem ipsum", null, false, false));
        uniqueInsideBuildingManager.refresh();
    }

    @Before
    public void setXmlToFile() throws ApplicationException {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
    }

    @After
    public void resetProfileService() {
        userService.setProfileService(profileService);
    }

    @After
    public void setServiceRight() {
        mapService.setD2nXmlReader(d2NXmlReader);
    }

    @Test
    public void cityDoesNotExist() {
        try {
            applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 666, 1040, 1);
            fail("Should have thrown an exception because the city does not exist");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CITY_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void buildingDoesNotExist() {
        try {
            applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 12345, 1666, 1);
            fail("Should have thrown an exception because the building does not exist");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.BUILDING_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void unlockBuilding() throws ApplicationException {
        applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 12345, 1042, 1);
        City city = cityDao.findCity(12345);
        List<InsideBuilding> buildings = insideBuildingDao.findInsideBuildings(12345);
        assertEquals(1, buildings.size());
        assertEquals(InsideBuildingStatus.AVAILABLE, buildings.get(0).getStatus());
    }

    @Test
    public void lockBuilding() throws ApplicationException {
        unlockBuilding();
        applicationContext.getBean(BuildingActionLock.class).execute(new UserKey("cafebabe"), 12345, 1042, 1);
        City city = cityDao.findCity(12345);
        assertEquals(0, insideBuildingDao.findInsideBuildings(12345).size());
    }

    @Test
    public void multipleBuildingActions() throws ApplicationException {
        applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 12345, 1042, 1);
        applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 12345, 1050, 1);
        applicationContext.getBean(BuildingActionLock.class).execute(new UserKey("cafebabe"), 12345, 1050, 1);
        applicationContext.getBean(BuildingActionUnlock.class).execute(new UserKey("cafebabe"), 12345, 1062, 1);
        City city = cityDao.findCity(12345);
        List<InsideBuilding> buildings = insideBuildingDao.findInsideBuildings(12345);
        assertEquals(2, buildings.size());
        assertEquals(InsideBuildingStatus.AVAILABLE, buildings.get(0).getStatus());
        assertEquals(InsideBuildingStatus.AVAILABLE, buildings.get(1).getStatus());
    }
    
}
