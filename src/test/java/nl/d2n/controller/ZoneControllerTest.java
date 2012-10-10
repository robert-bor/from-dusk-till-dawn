package nl.d2n.controller;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.SiteKeyChecker;
import nl.d2n.model.Zone;
import nl.d2n.service.ExternalApplicationAuthenticator;
import nl.d2n.service.MapService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ZoneControllerTest extends SpringContextTestCase {

    @Autowired
    private ZoneController zoneController;

    @Autowired
    ExternalApplicationAuthenticator authenticator;

    @Before
    public void insertTestKeys() {
        Properties properties = new Properties();
        properties.setProperty(SiteKeyChecker.EXTERNAL_APPLICATION_PREFIX+".nitelight", "CAFEBABE");
        SiteKeyChecker checker = new SiteKeyChecker(properties);
        authenticator.setSiteKeyChecker(checker);
    }

    @Test
    public void testWithEmptySiteKey() throws Exception {
        testSiteKey("", D2NErrorCode.UNKNOWN_SITE_KEY);
    }

    @Test
    public void testWithUnknownSiteKey() throws Exception {
        testSiteKey("EBABEFAC", D2NErrorCode.UNKNOWN_SITE_KEY);
    }

    @Test
    public void testWikiKey() throws Exception {
        zoneController.setMapService(new MapService() {
            public Map<Integer, Map<Integer, Zone>> getZones(int cityId) throws ApplicationException {
                return new TreeMap<Integer, Map<Integer, Zone>>();
            }
        });
        zoneController.getZoneInfo(new MockHttpServletRequest(), new MockHttpServletResponse(), "CAFEBABE", 42, null, null);
    }

    protected void testSiteKey(String siteKey, D2NErrorCode expectedError) throws Exception {
        try {
            zoneController.getZoneInfo(null, null, siteKey, 42, null, null);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(expectedError, err.getError());
        }
    }
}
