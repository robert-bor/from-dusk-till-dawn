package nl.d2n.service.actions;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.UserActionDao;
import nl.d2n.model.*;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.D2NXmlReaderFromFile;
import nl.d2n.service.MapService;
import nl.d2n.service.ProfileService;
import nl.d2n.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class MapActionReadTest extends SpringContextTestCase {

    @Autowired
    private MapService mapService;

    @Autowired
    private D2NXmlReader d2NXmlReader;

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private UserActionDao userActionDao;

    @Autowired
    private UserService userService;

    @Before
    public void stubProfileService() {
        userService.setProfileService(new ProfileService() {
            public void updateProfile(User user) throws ApplicationException {}
        });
    }

    @Before
    public void setServiceRight() {
        mapService.setD2nXmlReader(d2NXmlReader);
    }

    @Test
    public void upgradedMap() throws Exception {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("upgraded-map.xml"));
        Info info = applicationContext.getBean(MapActionRead.class).execute(new UserKey("cafebabe")).getInfo();
        Map<Integer, Map<Integer, Zone>> zones = info.getMatrix().toJsonMap();
        Zone zone = zones.get(4).get(-7); // Search for "Construction Site" in xml doc
        assertEquals(0, zone.getZombies());
        assertEquals(ZoneDanger.NONE, zone.getDanger());
        zone = zones.get(4).get(-8); // 4/-8
        assertEquals(-1, zone.getZombies());
        assertEquals(ZoneDanger.UNKNOWN, zone.getDanger());
        zone = zones.get(4).get(-2); // 4/-2
        assertEquals(2, zone.getZombies());
        assertEquals(ZoneDanger.ONE_TO_THREE, zone.getDanger());
    }

    @Test
    public void readInfoFirstTimeUser() throws Exception  {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("sample-s3.xml"));
        try {
            applicationContext.getBean(MapActionRead.class).execute(new UserKey("cafebabe"));
        } catch (ApplicationException err) {
            System.out.println(err.getError());
        }
        UserAction action = userActionDao.findAllActions().get(0);
        assertEquals(UpdateAction.READ_MAP, action.getAction());
    }

}
