package nl.d2n.model;

import org.junit.Test;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class SiteKeyCheckerTest {

    @Test
    public void siteKeyUnknown() {
        SiteKeyChecker checker = new SiteKeyChecker(new Properties());
        try {
            checker.getExternalApplication("unknown-key");
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.UNKNOWN_SITE_KEY, err.getError());
        }
    }

    @Test
    public void getApplication() throws ApplicationException {
        Properties properties = new Properties();
        properties.setProperty(SiteKeyChecker.EXTERNAL_APPLICATION_PREFIX+".wiki", "CAFEBABE");
        SiteKeyChecker checker = new SiteKeyChecker(properties);
        assertEquals(ExternalApplication.WIKI, checker.getExternalApplication("CAFEBABE"));
    }

}
