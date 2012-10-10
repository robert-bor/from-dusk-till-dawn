package nl.d2n.service.actions;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.User;
import nl.d2n.model.UserKey;
import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.D2NXmlReaderFromFile;
import nl.d2n.service.MapService;
import nl.d2n.service.ProfileService;
import nl.d2n.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class ZoneAction extends SpringContextTestCase {

    @Autowired
    protected MapService mapService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private D2NXmlReader d2NXmlReader;

    @Autowired
    protected ZoneDao zoneDao;

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void stubProfileService() {
        userService.setProfileService(new ProfileService() {
            public void updateProfile(User user) throws ApplicationException {}
        });
    }

    protected void readXml() throws ApplicationException {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("outside-town.xml"));
        applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(new UserKey("cafebabe")); // This sets the security
    }

    @After
    public void resetProfileService() {
        userService.setProfileService(profileService);
    }

    @After
    public void setServiceRight() {
        mapService.setD2nXmlReader(d2NXmlReader);
    }
}
